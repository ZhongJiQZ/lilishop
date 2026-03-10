package cn.lili.modules.circle.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 圈子帖子点赞记录
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_circle_post_like")
@Schema(description = "圈子帖子点赞记录")
public class CirclePostLike extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "帖子ID")
    private Long postId;

    @Schema(description = "点赞用户ID")
    private Long userId;

//    @Schema(description = "点赞时间")
//    private LocalDateTime createTime;
}