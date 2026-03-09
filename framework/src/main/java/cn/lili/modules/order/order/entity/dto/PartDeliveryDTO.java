package cn.lili.modules.order.order.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 部分发货
 */
@Data
public class PartDeliveryDTO {

    @Schema(description = "订单货物Id")
    private String orderItemId;

    @Schema(description = "发货数量")
    private Integer deliveryNum;


}
