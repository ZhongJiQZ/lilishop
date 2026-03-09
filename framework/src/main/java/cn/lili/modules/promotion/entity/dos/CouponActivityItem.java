package cn.lili.modules.promotion.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 优惠券活动实体类
 *
 * @author Bulbasaur
 * @since 2020-03-19 10:44 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_coupon_activity_item")
@Schema(description = "优惠券活动-优惠券关联实体类")
public class CouponActivityItem extends BaseEntity {

    @Schema(description = "优惠券活动ID")
    private String activityId;

    @Schema(description = "优惠券ID")
    private String couponId;

    @Schema(description = "优惠券数量")
    private Integer num;


}