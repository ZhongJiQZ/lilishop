package cn.lili.modules.procurement.serviceimpl;

import cn.lili.modules.procurement.entity.dos.ProcurementOrderItem;
import cn.lili.modules.procurement.mapper.ProcurementOrderItemMapper;
import cn.lili.modules.procurement.service.ProcurementOrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 采购单明细业务实现
 * 提供采购明细的批量保存与初始化
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Service
public class ProcurementOrderItemServiceImpl extends ServiceImpl<ProcurementOrderItemMapper, ProcurementOrderItem> implements ProcurementOrderItemService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveItems(String orderId, List<ProcurementOrderItem> items) {
        for (ProcurementOrderItem item : items) {
            item.setProcurementOrderId(orderId);
            if (item.getReceivedQuantity() == null) {
                item.setReceivedQuantity(0);
            }
        }
        return this.saveBatch(items);
    }
}
