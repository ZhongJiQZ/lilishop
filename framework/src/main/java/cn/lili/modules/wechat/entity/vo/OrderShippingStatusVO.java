package cn.lili.modules.wechat.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 订单发货状态 VO（微信确认收货回调校验专用）
 */
@Data
@Schema(description = "订单发货状态 VO")
public class OrderShippingStatusVO {

    @Schema(description = "订单编号")
    private String orderSn;

    @Schema(description = "是否已发货")
    private Boolean shipStatus;

    @Schema(description = "是否已确认收货")
    private Boolean confirmReceiveStatus;

    @Schema(description = "微信是否已上报发货")
    private Boolean wxUploadShipping;

    @Schema(description = "微信是否已上报确认收货")
    private Boolean wxConfirmReceive;

    @Schema(description = "订单状态")
    private String orderStatus;
}
