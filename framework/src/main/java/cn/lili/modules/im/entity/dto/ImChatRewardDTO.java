package cn.lili.modules.im.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 聊天打赏dto
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
@Data
@Schema(description = "聊天打赏dto")
public class ImChatRewardDTO {
    /**
     * 礼物ID
     */
    @Schema(description = "礼物ID")
    private String giftId;
    /**
     * 试穿员ID（店铺ID）
     */
    @Schema(description = "试穿员ID（店铺ID）")
    private String toMemberId;
    /**
     * 数量
     */
    @Schema(description = "数量")
    private Integer num = 1;
}
