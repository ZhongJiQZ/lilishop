package cn.lili.modules.search.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.cache.Cache;
import cn.lili.cache.CachePrefix;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.goods.entity.dto.GoodsSearchParams;
import cn.lili.modules.goods.entity.enums.GoodsAuthEnum;
import cn.lili.modules.goods.entity.enums.GoodsStatusEnum;
import cn.lili.modules.goods.service.GoodsSkuService;
import cn.lili.modules.search.entity.dos.EsGoodsIndex;
import cn.lili.modules.search.entity.dos.EsGoodsRelatedInfo;
import cn.lili.modules.search.entity.dto.EsGoodsSearchDTO;
import cn.lili.modules.search.service.EsGoodsSearchAbstractService;
import cn.lili.modules.search.service.EsGoodsSearchService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ES商品搜索业务层实现
 *
 * @author paulG
 * @since 2020/10/16
 **/
@Slf4j
@Service
public class EsGoodsSearchServiceImpl extends EsGoodsSearchAbstractService implements EsGoodsSearchService {

    private static final String ATTR_PATH = "attrList";
    private static final String ATTR_VALUE = "attrList.value.keyword";
    private static final String ATTR_NAME = "attrList.name.keyword";
    private static final String ATTR_SORT = "attrList.sort";
    /**
     * ES
     */
    @Autowired
    private ElasticsearchOperations restTemplate;
    /**
     * 缓存
     */
    @Autowired
    private Cache<Object> cache;

    @Autowired
    private GoodsSkuService goodsSkuService;

    @Override
    public SearchPage<EsGoodsIndex> searchGoods(EsGoodsSearchDTO searchDTO, PageVO pageVo) {

        // 判断商品索引是否存在
        if (!client.indexOps(EsGoodsIndex.class).exists()) {
            return null;
        }

        if (CharSequenceUtil.isNotBlank(searchDTO.getKeyword())) {
            cache.incrementScore(CachePrefix.HOT_WORD.getPrefix(), searchDTO.getKeyword());
        }
        NativeQueryBuilder searchQueryBuilder = createSearchQueryBuilder(searchDTO, pageVo);
        // 根据商品id collapse（搜索结果根据spu 展示）
//        searchQueryBuilder.withFieldCollapse(FieldCollapse.of(f -> f.field("goodsId.keyword")));
        //2.2 指定高亮
        HighlightField highlightField = new HighlightField(GOODS_NAME);
        HighlightParameters highlightParameters = HighlightParameters.builder().withPostTags("</font>").withPreTags("<font color='red'>").build();

        Highlight highlight = new Highlight(highlightParameters, List.of(highlightField));
        HighlightQuery highlightBuilder = new HighlightQuery(highlight, EsGoodsIndex.class);
        searchQueryBuilder.withHighlightQuery(highlightBuilder);
        SearchHits<EsGoodsIndex> search; // Change from SearchHits<T> to SearchHits<EsGoodsIndex>
        try {
            search = client.search(searchQueryBuilder.build(), EsGoodsIndex.class);
        } catch (Exception e) {
            // 打印根因，便于定位 all shards failed 的具体问题
            Throwable root = e;
            while (root.getCause() != null) {
                root = root.getCause();
            }
            log.error("商品索引搜索出现异常，尝试降级查询。根因: {}。请到管理端 -> 系统设置 -> 商品设置 -> 重新生成所有商品索引！", root.getMessage(), e);
            try {
                // 一级降级：安全查询（不使用自定义 analyzer/子字段），且移除高亮
                NativeQueryBuilder safeBuilder = createSafeSearchQueryBuilder(searchDTO, pageVo);
                search = client.search(safeBuilder.build(), EsGoodsIndex.class);
            } catch (Exception e2) {
                // 再次打印根因
                Throwable root2 = e2;
                while (root2.getCause() != null) {
                    root2 = root2.getCause();
                }
                log.warn("ES一级降级查询失败，根因: {}。继续使用超安全 match_all。", root2.getMessage(), e2);
                try {
                    // 二级降级：超安全查询（仅 match_all），无高亮、无评分函数
                    NativeQueryBuilder superSafeBuilder = createSuperSafeSearchQueryBuilder(pageVo);
                    search = client.search(superSafeBuilder.build(), EsGoodsIndex.class);
                } catch (Exception e3) {
                    Throwable root3 = e3;
                    while (root3.getCause() != null) {
                        root3 = root3.getCause();
                    }
                    log.error("ES二级降级查询失败，根因: {}", root3.getMessage(), e3);
                    throw new ServiceException("搜索出现异常，请联系管理员！");
                }
            }
        }

        return SearchHitSupport.searchPageFor(search, searchQueryBuilder.getPageable());
    }

    @Override
    public <T> SearchPage<T> searchGoods(Query searchQuery, Class<T> clazz) {
        SearchHits<T> search = restTemplate.search(searchQuery, clazz);
        return SearchHitSupport.searchPageFor(search, searchQuery.getPageable());
    }

    @Override
    public Page<EsGoodsIndex> searchGoodsByPage(EsGoodsSearchDTO searchDTO, PageVO pageVo) {
        if (pageVo == null) {
            pageVo = new PageVO();
        }
        if (searchDTO != null && CharSequenceUtil.isNotBlank(searchDTO.getKeyword())) {
            cache.incrementScore(CachePrefix.HOT_WORD.getPrefix(), searchDTO.getKeyword());
        }

        GoodsSearchParams params = new GoodsSearchParams();
        if (searchDTO != null) {
            if (CharSequenceUtil.isNotEmpty(searchDTO.getStoreId())) {
                params.setStoreId(searchDTO.getStoreId());
            }
            if (CharSequenceUtil.isNotEmpty(searchDTO.getKeyword())) {
                params.setGoodsName(searchDTO.getKeyword());
            }
            if (CharSequenceUtil.isNotEmpty(searchDTO.getCategoryId())) {
                params.setCategoryPath(searchDTO.getCategoryId());
            }
            if (CharSequenceUtil.isNotEmpty(searchDTO.getStoreCatId())) {
                params.setStoreCategoryPath(searchDTO.getStoreCatId());
            }
            if (CharSequenceUtil.isNotEmpty(searchDTO.getPrice())) {
                params.setPrice(searchDTO.getPrice());
            }
            if (searchDTO.getRecommend() != null) {
                params.setRecommend(searchDTO.getRecommend());
            }
            if (CharSequenceUtil.isNotEmpty(searchDTO.getGoodsType())) {
                params.setGoodsType(searchDTO.getGoodsType());
            }
            if (CharSequenceUtil.isNotEmpty(searchDTO.getSalesModel())) {
                params.setSalesModel(searchDTO.getSalesModel());
            }
            if (CharSequenceUtil.isNotEmpty(searchDTO.getIds())) {
                params.setId(searchDTO.getIds());
            }
        }

        QueryWrapper<GoodsSku> qw = params.queryWrapper();
        String storeId = searchDTO != null ? searchDTO.getStoreId() : null;
        if (CharSequenceUtil.isNotEmpty(storeId)) {
            qw.apply("goods_id IN (SELECT id FROM li_goods WHERE store_id = {0} AND delete_flag = {1} AND auth_flag = {2} AND market_enable = {3})",
                    storeId, false, GoodsAuthEnum.PASS.name(), GoodsStatusEnum.UPPER.name());
        } else {
            qw.apply("goods_id IN (SELECT id FROM li_goods WHERE delete_flag = {0} AND auth_flag = {1} AND market_enable = {2})",
                    false, GoodsAuthEnum.PASS.name(), GoodsStatusEnum.UPPER.name());
        }

        if (searchDTO != null) {
            if (CharSequenceUtil.isNotEmpty(searchDTO.getBrandId())) {
                qw.in("brand_id", Arrays.asList(searchDTO.getBrandId().split("@")));
            }
            if (CharSequenceUtil.isNotEmpty(searchDTO.getNeGoodsType())) {
                qw.ne("goods_type", searchDTO.getNeGoodsType());
            }
            if (CharSequenceUtil.isNotEmpty(searchDTO.getNeSalesModel())) {
                qw.ne("sales_model", searchDTO.getNeSalesModel());
            }
        }

        Page<GoodsSku> mpPage = PageUtil.initPage(pageVo);
        if (CharSequenceUtil.isBlank(pageVo.getSort())) {
            mpPage.addOrder(OrderItem.desc("create_time"));
        }

        IPage<GoodsSku> skuPage = goodsSkuService.page(mpPage, qw);

        Page<EsGoodsIndex> resultPage = new Page<>();
        resultPage.setCurrent(skuPage.getCurrent());
        resultPage.setSize(skuPage.getSize());
        resultPage.setTotal(skuPage.getTotal());
        resultPage.setPages(skuPage.getPages());
        if (skuPage.getRecords() == null || skuPage.getRecords().isEmpty()) {
            resultPage.setRecords(Collections.emptyList());
        } else {
            resultPage.setRecords(skuPage.getRecords().stream().map(EsGoodsIndex::new).collect(Collectors.toList()));
        }
        return resultPage;
    }

    @Override
    public EsGoodsRelatedInfo getSelector(EsGoodsSearchDTO goodsSearch, PageVO pageVo) {
        // 判断商品索引是否存在
        if (!client.indexOps(EsGoodsIndex.class).exists()) {
            return null;
        }

        NativeQueryBuilder searchQueryBuilder = createSearchQueryBuilder(goodsSearch, null);
        //分类
        searchQueryBuilder.withAggregation(ATTR_CATEGORY_PATH_AGG, Aggregation.of(
                a -> a.terms(t -> t.field(ATTR_CATEGORY_PATH_FIELD)).aggregations(ATTR_CATEGORY_NAME_AGG, Aggregation.of(
                        a1 -> a1.terms(t -> t.field(ATTR_CATEGORY_NAME_FIELD)))
                )
        ));

        //品牌
        searchQueryBuilder.withAggregation(ATTR_BRAND_ID_NAME_AGG, Aggregation.of(
                a -> a.terms(t -> t.field(ATTR_BRAND_ID_FIELD).size(1000)).aggregations(ATTR_BRAND_NAME_AGG, Aggregation.of(
                        a1 -> a1.terms(t -> t.field(ATTR_BRAND_NAME_FIELD)))
                )
        ));

        searchQueryBuilder.withAggregation(ATTR_BRAND_ID_URL_AGG, Aggregation.of(
                a -> a.terms(t -> t.field(ATTR_BRAND_ID_FIELD).size(1000)).aggregations(ATTR_BRAND_URL_AGG, Aggregation.of(
                        a1 -> a1.terms(t -> t.field(ATTR_BRAND_URL_FIELD)))
                )
        ));
        //参数
        searchQueryBuilder.withAggregation(ATTR_AGG, Aggregation.of(
                n -> n
                        .nested(n1 -> n1.path(ATTR_PATH))
                        .aggregations("nameAgg", a -> a.terms(t -> t.field(ATTR_NAME).size(1000))
                                .aggregations("valueAgg", Aggregation.of(a2 -> a2.terms(t -> t.field(ATTR_VALUE))))
                                .aggregations("orderAgg", a3 -> a3.sum(s -> s.field(ATTR_SORT)))
                                .aggregations("sortAgg", a4 -> a4.bucketSort(b -> b.sort(s -> s.field(f -> f.field("orderAgg").order(SortOrder.Asc)))))
                        )

        ));

        searchQueryBuilder.withMaxResults(0);
        SearchHits<EsGoodsIndex> search; // Change from SearchHits<T> to SearchHits<EsGoodsIndex>
        try {
            search = client.search(searchQueryBuilder.build(), EsGoodsIndex.class);
        } catch (Exception e) {
            log.error("商品索引搜索出现异常，请到管理端 -> 系统设置 -> 商品设置 -> 重新生成所有商品索引！", e);
            return new EsGoodsRelatedInfo();
        }

        log.debug("getSelector DSL:{}", searchQueryBuilder.getQuery());
        log.debug("getSelector DSL:{}", searchQueryBuilder.getAggregations());
        Map<String, ElasticsearchAggregation> aggregationMap = Objects.requireNonNull((ElasticsearchAggregations) search.getAggregations()).aggregationsAsMap();
        return convertToEsGoodsRelatedInfo(aggregationMap, goodsSearch);
    }

    @Override
    public List<EsGoodsIndex> getEsGoodsBySkuIds(List<String> skuIds, PageVO pageVo) {
        List<MultiGetItem<EsGoodsIndex>> multiGetItems = client.multiGet(client.idsQuery(skuIds), EsGoodsIndex.class, client.getIndexCoordinatesFor(EsGoodsIndex.class));
        return multiGetItems.stream().map(MultiGetItem::getItem).toList();
    }

    /**
     * 根据id获取商品索引
     *
     * @param id 商品skuId
     * @return 商品索引
     */
    @Override
    public EsGoodsIndex getEsGoodsById(String id) {
        return this.restTemplate.get(id, EsGoodsIndex.class);
    }

}
