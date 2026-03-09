package cn.lili.modules.store.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 店铺设置
 *
 * @author Bulbasaur
 * @since 2020/12/16 15:15
 */
@Data
public class StoreSettingDTO {

    @Schema(description = "店铺logo")
    private String storeLogo;

    @Schema(description = "店铺简介")
    private String storeDesc;

    @Schema(description = "地址id，'，'分割 ")
    private String storeAddressIdPath;

    @Schema(description = "地址名称， '，'分割")
    private String storeAddressPath;

    @Schema(description = "详细地址")
    private String storeAddressDetail;

    @Schema(description = "经纬度")
    private String storeCenter;

    @Schema(description = "默认页面是否开启")
    private Boolean pageShow;

    @Schema(description = "是否开启自提")
    private Boolean selfPickFlag;

}
