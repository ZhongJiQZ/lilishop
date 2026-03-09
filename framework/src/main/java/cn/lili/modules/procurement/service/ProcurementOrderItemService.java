package cn.lili.modules.procurement.service;

import cn.lili.modules.procurement.entity.dos.ProcurementOrderItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 采购单明细业务接口
 * 定义采购明细批量保存等能力
 * @author Bulbasaur
 * @since 2025-12-18
 */
public interface ProcurementOrderItemService extends IService<ProcurementOrderItem> {
    /**
     * 批量保存采购单明细
     * @param orderId 采购单ID
     * @param items 采购明细列表
     * @return 是否保存成功
     */
    boolean saveItems(String orderId, List<ProcurementOrderItem> items);
}
