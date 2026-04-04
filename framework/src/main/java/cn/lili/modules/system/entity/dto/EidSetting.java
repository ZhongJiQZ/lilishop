package cn.lili.modules.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 平台币设置
 *
 * @author lensing
 * @since 2026/03/25 7:59 下午
 */
@Data
public class EidSetting implements Serializable {

    @Schema(description = "腾讯云SecretId")
    private String secretId;

    @Schema(description = "腾讯云SecretKey")
    private String secretKey;

    @Schema(description = "E证通商户ID")
    private String merchantId;

    @Schema(description = "地域")
    private String region;

    @Schema(description = "是否开启")
    private Boolean isOpen;

}
