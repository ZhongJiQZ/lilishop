package cn.lili.modules.procurement.service;

import cn.lili.modules.procurement.entity.dos.StockReason;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 出入库原因业务接口
 * 定义分页查询等业务能力
 * @author Bulbasaur
 * @since 2025-12-18
 */
public interface StockReasonService extends IService<StockReason> {
    /**
     * 分页查询出入库原因
     * @param reason 原因关键词
     * @param category 类别
     * @param page 分页参数
     * @return 出入库原因分页数据
     */
    IPage<StockReason> page(String reason, String category, Page<StockReason> page);
}
