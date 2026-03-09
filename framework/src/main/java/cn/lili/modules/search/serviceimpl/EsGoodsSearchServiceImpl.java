package cn.lili.modules.search.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.cache.Cache;
import cn.lili.cache.CachePrefix;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.search.entity.dos.EsGoodsIndex;
import cn.lili.modules.search.entity.dos.EsGoodsRelatedInfo;
import cn.lili.modules.search.entity.dto.EsGoodsSearchDTO;
import cn.lili.modules.search.service.EsGoodsSearchAbstractService;
import cn.lili.modules.search.service.EsGoodsSearchService;
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
        // 判断商品索引是否存在
        if (!restTemplate.indexOps(EsGoodsIndex.class).exists()) {
            return null;
        }

        SearchPage<EsGoodsIndex> esGoodsIndices = this.searchGoods(searchDTO, pageVo);
        Page<EsGoodsIndex> resultPage = new Page<>();
        if (esGoodsIndices != null && !esGoodsIndices.getContent().isEmpty()) {
            List<EsGoodsIndex> collect = esGoodsIndices.getSearchHits().getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
            resultPage.setRecords(collect);
            resultPage.setPages(esGoodsIndices.getTotalPages());
            resultPage.setCurrent(esGoodsIndices.getNumber() + 1L);
            resultPage.setSize(esGoodsIndices.getSize());
            resultPage.setTotal(esGoodsIndices.getTotalElements());
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
