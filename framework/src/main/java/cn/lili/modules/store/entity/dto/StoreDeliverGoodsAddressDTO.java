package cn.lili.modules.store.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 店铺发货地址
 *
 * @author chc
 * @since 2022-4-12 10:14:05
 */
@Data
public class StoreDeliverGoodsAddressDTO {

    @Schema(description = "发货人姓名")
    private String salesConsignorName;

    @Schema(description = "发货人手机号")
    private String salesConsignorMobile;

    @Schema(description = "地址Id， '，'分割")
    private String salesConsignorAddressId;

    @Schema(description = "地址名称， '，'分割")
    private String salesConsignorAddressPath;

    @Schema(description = "详细地址")
    private String salesConsignorDetail;

}
