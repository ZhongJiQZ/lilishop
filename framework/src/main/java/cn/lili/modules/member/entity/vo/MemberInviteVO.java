package cn.lili.modules.member.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "推广明细VO")
public class MemberInviteVO {

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户头像")
    private String face;

    @Schema(description = "注册时间（MM-dd HH:mm）")
    private String registerTime;

    @Schema(description = "消费金额")
    private BigDecimal consumeAmount;

    @Schema(description = "状态：未消费/待确认/已消费")
    private String status;
}
