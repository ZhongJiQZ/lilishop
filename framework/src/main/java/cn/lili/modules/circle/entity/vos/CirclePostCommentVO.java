package cn.lili.modules.circle.entity.vos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 圈子帖子评论VO
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@Data
public class CirclePostCommentVO {

    private static final long serialVersionUID = 6377623919990713567L;

    @Schema(description = "评论ID")
    private String id;

    @JsonIgnore
    @Schema(description = "评论认ID", hidden = true)
    private String userId;

    @Schema(description = "评论人")
    private String nickName;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "是否可删除")
    private Boolean canDelete;
}
