package cn.lili.modules.store.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 店铺售后收件地址
 *
 * @author pikachu
 * @since 2020-08-22 15:10:51
 */
@Data
public class StoreAfterSaleAddressDTO {

    @Schema(description = "收货人姓名")
    private String salesConsigneeName;

    @Schema(description = "收件人手机")
    private String salesConsigneeMobile;

    @Schema(description = "地址Id， '，'分割")
    private String salesConsigneeAddressId;

    @Schema(description = "地址名称， '，'分割")
    private String salesConsigneeAddressPath;

    @Schema(description = "详细地址")
    private String salesConsigneeDetail;
}
