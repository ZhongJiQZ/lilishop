package cn.lili.modules.circle.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺粉丝记录
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_circle_post_follow")
@Schema(description = "店铺粉丝记录")
public class CirclePostFollow extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "关注者ID（当前用户）")
    private String memberId;

    @Schema(description = "被关注者ID（作者）")
    private String followedMemberId;
}