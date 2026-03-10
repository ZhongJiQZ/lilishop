package cn.lili.modules.circle.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 圈子帖子浏览记录
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_circle_post_view_record")
@Schema(description = "圈子帖子浏览记录")
public class CirclePostViewRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "帖子ID")
    private Long postId;

    @Schema(description = "浏览用户ID，0表示游客")
    private Long userId = 0L;

    @Schema(description = "IP地址")
    private String ip;

//    @Schema(description = "浏览时间")
//    private LocalDateTime createTime;
}