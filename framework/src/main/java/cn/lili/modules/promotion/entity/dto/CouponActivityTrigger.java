package cn.lili.modules.promotion.entity.dto;

import cn.lili.modules.promotion.entity.enums.CouponActivityTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 优惠券活动触发
 *
 * @author paulG
 * @since 2020/10/9
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponActivityTrigger {


    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "优惠券活动类型")
    private CouponActivityTypeEnum couponActivityTypeEnum;
}
