package cn.lili.modules.order.order.entity.dto;

import cn.hutool.core.date.DateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单Item操作DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemOperationDTO {

    @Schema(description = "订单完成时间")
    DateTime receiveTime;

    @Schema(description = "订单售后状态")
    String afterSaleStatus;

    @Schema(description = "订单评价状态")
    String commentStatus;

    @Schema(description = "订单投诉状态")
    String complainStatus;

}
