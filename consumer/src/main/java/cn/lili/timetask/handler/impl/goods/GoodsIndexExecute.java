package cn.lili.timetask.handler.impl.goods;

import cn.lili.modules.search.service.EsGoodsIndexService;
import cn.lili.timetask.handler.EveryDayExecute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 每日12点 自动重建ES商品索引
 * 等价于：后台手动点击【ES初始化接口】->【init】
 */
@Slf4j
@Component
public class GoodsIndexExecute implements EveryDayExecute {

    @Autowired
    private EsGoodsIndexService esGoodsIndexService;

    @Override
    public void execute() {
        log.info("=====================================");
        log.info(" 每日12点 → 自动执行ES商品索引初始化");
        log.info("=====================================");

        try {
            // ✅ 这就是你后台点击的 init() 方法！
            esGoodsIndexService.init();
            log.info("✅ 每日商品索引自动初始化完成！");
        } catch (Exception e) {
            log.error("❌ 每日商品索引自动初始化失败", e);
        }
    }
}