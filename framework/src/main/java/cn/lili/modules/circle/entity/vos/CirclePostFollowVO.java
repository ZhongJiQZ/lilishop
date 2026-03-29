package cn.lili.modules.circle.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 店铺粉丝VO
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
@Data
public class CirclePostFollowVO {

    private static final long serialVersionUID = 6377623919990713567L;

    @Schema(description = "店铺logo")
    private String storeLogo;

    @Schema(description = "店铺名称")
    private String storeName;

}
