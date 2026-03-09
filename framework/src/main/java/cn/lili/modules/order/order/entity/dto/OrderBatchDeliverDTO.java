package cn.lili.modules.order.order.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 订单批量发货DTO
 *
 * @author Bulbasaur
 * @since 2021/5/26 4:21 下午
 */
@Data
public class OrderBatchDeliverDTO {

    @Schema(description = "订单SN")
    private String orderSn;

    @Schema(description = "物流公司ID")
    private String logisticsId;

    @Schema(description = "物流公司名称")
    private String logisticsName;

    @Schema(description = "发货单号")
    private String logisticsNo;

}
