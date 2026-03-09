package cn.lili.modules.procurement.serviceimpl;

import cn.lili.modules.procurement.entity.dos.ProcurementInboundItem;
import cn.lili.modules.procurement.mapper.ProcurementInboundItemMapper;
import cn.lili.modules.procurement.service.ProcurementInboundItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 采购入库明细业务实现
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Service
public class ProcurementInboundItemServiceImpl extends ServiceImpl<ProcurementInboundItemMapper, ProcurementInboundItem> implements ProcurementInboundItemService {
}
