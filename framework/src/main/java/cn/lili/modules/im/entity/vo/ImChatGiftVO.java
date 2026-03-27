package cn.lili.modules.im.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * MessageVO
 *
 * @author lensing
 * @version v1.0
 * 2026-03-25 15:51
 */
@Data
public class ImChatGiftVO {

    @Schema(description = "礼物ID")
    private String id;

    @Schema(description = "礼物名称")
    private String giftName;

    @Schema(description = "礼物图标")
    private String giftImage;

    @Schema(description = "价格(平台币)")
    private BigDecimal coinPrice;
}
