package cn.lili.modules.goods.serviceimpl;

import cn.hutool.core.collection.CollUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.goods.entity.vos.BatchCopyTemplateGoodsResultVO;
import cn.lili.modules.goods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 管理端批量复制模板商品（通过代理调用 {@link GoodsService#copyMinimalGoodsFromTemplate}，保证事务生效）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminTemplateGoodsBatchCopyService {

    private static final int MAX_DETAILS = 80;

    private final GoodsService goodsService;

    public BatchCopyTemplateGoodsResultVO batchCopy(List<String> templateGoodsIds, List<String> targetStoreIds) {
        if (CollUtil.isEmpty(templateGoodsIds) || CollUtil.isEmpty(targetStoreIds)) {
            throw new ServiceException("请选择模板商品与目标店铺");
        }
        String templateStoreId = goodsService.getTemplateStoreId();
        Set<String> goodsIds = new LinkedHashSet<>(templateGoodsIds);
        Set<String> storeIds = new LinkedHashSet<>(targetStoreIds);
        storeIds.remove(templateStoreId);
        if (storeIds.isEmpty()) {
            throw new ServiceException("请至少选择一个非模板店铺作为复制目标");
        }

        BatchCopyTemplateGoodsResultVO vo = new BatchCopyTemplateGoodsResultVO();
        List<String> details = new ArrayList<>();
        vo.setDetails(details);

        int success = 0;
        int skipped = 0;
        int failed = 0;

        for (String storeId : storeIds) {
            for (String goodsId : goodsIds) {
                try {
                    goodsService.copyMinimalGoodsFromTemplate(goodsId, storeId);
                    success++;
                } catch (ServiceException e) {
                    String msg = e.getMsg() != null ? e.getMsg() : "";
                    if (msg.contains("重复") || msg.contains("已复制")) {
                        skipped++;
                        appendDetail(details, "跳过(已复制): 店铺=" + storeId + " 商品=" + goodsId);
                    } else {
                        failed++;
                        appendDetail(details, "失败: 店铺=" + storeId + " 商品=" + goodsId + " — " + msg);
                        log.warn("batch copy failed store={} goods={} {}", storeId, goodsId, msg);
                    }
                } catch (Exception ex) {
                    failed++;
                    appendDetail(details, "失败: 店铺=" + storeId + " 商品=" + goodsId + " — " + ex.getMessage());
                    log.error("batch copy error store={} goods={}", storeId, goodsId, ex);
                }
            }
        }

        vo.setSuccessCount(success);
        vo.setSkippedCount(skipped);
        vo.setFailedCount(failed);
        return vo;
    }

    private static void appendDetail(List<String> details, String line) {
        if (details.size() < MAX_DETAILS) {
            details.add(line);
        }
    }
}
