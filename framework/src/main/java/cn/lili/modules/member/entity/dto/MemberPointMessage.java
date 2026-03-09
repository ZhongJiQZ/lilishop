package cn.lili.modules.member.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员积分
 *
 * @author Bulbasaur
 * @since 2020/12/14 16:31
 */
@Data
public class MemberPointMessage {

    @Schema(description = "积分")
    private Long point;

    @Schema(description = "是否增加积分")
    private String type;

    @Schema(description = "会员id")
    private String memberId;
}
