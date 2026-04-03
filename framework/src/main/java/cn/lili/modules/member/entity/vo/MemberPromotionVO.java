package cn.lili.modules.member.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "推广中心统计VO")
public class MemberPromotionVO {

    @Schema(description = "累计邀请人数")
    private Integer totalInviteCount;

    @Schema(description = "本周新增邀请人数")
    private Integer weekNewInviteCount;

    @Schema(description = "累计消费总额")
    private BigDecimal totalConsumeAmount;

    @Schema(description = "本周新增消费总额")
    private BigDecimal weekNewConsumeAmount;

    @Schema(description = "累计佣金收益")
    private BigDecimal totalCommission;

    @Schema(description = "可提现余额")
    private BigDecimal canWithdrawAmount;

    @Schema(description = "邀请明细列表")
    private List<MemberInviteVO> inviteList;
}
