package cn.lili.modules.procurement.entity.params;

import cn.lili.common.vo.PageVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入库单查询参数
 * 支持按入库单号与采购订单ID过滤
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProcurementInboundSearchParams extends PageVO {

    @Schema(description = "单据编号")
    private String inboundSn;

    @Schema(description = "采购订单ID")
    private String procurementOrderId;
}
