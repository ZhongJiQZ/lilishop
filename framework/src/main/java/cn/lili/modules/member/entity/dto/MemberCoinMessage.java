package cn.lili.modules.member.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员平台币
 *
 * @author lensing
 * @since 2026/03/25 16:31
 */
@Data
public class MemberCoinMessage {

    @Schema(description = "平台币")
    private Long coin;

    @Schema(description = "是否增加平台币")
    private String type;

    @Schema(description = "会员id")
    private String memberId;
}
