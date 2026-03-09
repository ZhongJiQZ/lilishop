package cn.lili.modules.procurement.service;

import cn.lili.modules.procurement.entity.dos.ProcurementInbound;
import cn.lili.modules.procurement.entity.dto.ProcurementInboundCreateDTO;
import cn.lili.modules.procurement.entity.params.ProcurementInboundSearchParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 采购入库业务接口
 * 定义入库单创建、分页与详情查询能力
 * @author Bulbasaur
 * @since 2025-12-18
 */
public interface ProcurementInboundService extends IService<ProcurementInbound> {
    /**
     * 创建采购入库单
     * @param dto 采购入库单创建参数
     * @return 创建后的采购入库单
     */
    ProcurementInbound createInbound(ProcurementInboundCreateDTO dto);
    /**
     * 分页查询采购入库单
     * @param params 分页查询参数
     * @return 采购入库单分页数据
     */
    IPage<ProcurementInbound> page(ProcurementInboundSearchParams params);
    /**
     * 查询采购入库单详情
     * @param id 采购入库单ID
     * @return 采购入库单详情
     */
    ProcurementInbound getDetail(String id);
}
