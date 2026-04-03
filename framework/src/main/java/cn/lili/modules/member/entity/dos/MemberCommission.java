package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员佣金明细（邀请下级消费产生的佣金）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_member_commission")
@Schema(description = "会员佣金明细")
public class MemberCommission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单SN")
    private String orderSn;

    @Schema(description = "订单金额")
    private BigDecimal orderPrice;

    @Schema(description = "消费用户ID（下级）")
    private String memberId;

    @Schema(description = "消费用户昵称")
    private String memberName;

    @Schema(description = "佣金获得人ID（试穿员/上级）")
    private String commissionMemberId;

    @Schema(description = "佣金金额")
    private BigDecimal commission;

    @Schema(description = "佣金比例 10% = 0.10")
    private BigDecimal commissionRate;

    @Schema(description = "是否结算：默认0未结算，1已结算")
    private Integer isSettled;

    @Schema(description = "结算时间")
    private Date settleTime;
}
