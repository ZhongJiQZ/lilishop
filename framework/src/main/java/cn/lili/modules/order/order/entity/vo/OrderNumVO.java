package cn.lili.modules.order.order.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrderNumVO {

    @Schema(description = "未付款订单数量")
    private Integer waitPayNum;
    @Schema(description = "已付款订单数量")
    private Integer waitDeliveryNum;
    @Schema(description = "待发货订单数量")
    private Integer waitShipNum;
    @Schema(description = "部分发货订单数量")
    private Integer partsDeliveredNumNum;
    @Schema(description = "待收货订单数量")
    private Integer deliveredNum;
    @Schema(description = "待核验订单数量")
    private Integer waitCheckNum;
    @Schema(description = "待自提订单数量")
    private Integer waitSelfPickNum;
    @Schema(description = "已完成订单数量")
    private Integer finishNum;
    @Schema(description = "已关闭订单数量")
    private Integer closeNum;
}
