package cn.lili.modules.procurement.entity.vos;

import cn.lili.modules.procurement.entity.dos.ProcurementOrder;
import cn.lili.modules.procurement.entity.dos.ProcurementOrderItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 采购订单视图对象
 * 携带采购订单基础信息与明细项集合
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProcurementOrderVO extends ProcurementOrder {
    private List<ProcurementOrderItem> items;
}
