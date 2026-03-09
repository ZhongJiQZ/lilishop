package cn.lili.modules.order.order.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 部分发货参数封装
 *
 * @author liushuai(liushuai711 @ gmail.com)
 * @version v4.0
 * @Description:
 * @since 2022/10/29 17:52
 */
@Data
public class PartDeliveryParamsDTO {

    @Schema(description = "订单号")
    private String orderSn;

    @Schema(description = "发货单号")
    private String logisticsNo;

    @Schema(description = "发货方式")
    private String logisticsId;

    @Schema(description = "物流详细")
    private List<PartDeliveryDTO> partDeliveryDTOList;
}
