package cn.lili.modules.circle.entity.dos;

import cn.hutool.http.HtmlUtil;
import cn.lili.modules.circle.entity.dto.CirclePostCommentOperationDTO;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 圈子帖子评论
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_circle_post_comment")
@Schema(description = "圈子帖子评论")
public class CirclePostComment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "帖子ID")
    private String postId;

    @Schema(description = "评论人ID")
    private String userId;

    @Schema(description = "评论人类型：STORE=商家 MEMBER=普通用户")
    private String userType;

    @Schema(description = "评论内容", required = true)
    @NotEmpty(message = "评论内容不能为空")
    @Length(max = 500, message = "评论内容不能超过500个字符")
    private String content;

    @Schema(description = "父评论ID，0表示一级评论")
    private Long parentId = 0L;

    @Schema(description = "被回复的用户ID")
    private Long replyUserId = 0L;

    @Schema(description = "点赞数量")
    private Integer likeCount = 0;

    @Schema(description = "状态：1=正常 0=删除")
    private Integer status = 1;

    public CirclePostComment() {
    }

    public CirclePostComment(CirclePostCommentOperationDTO dto) {
        this.postId = dto.getPostId();
        this.content = dto.getContent();
    }

//    public static CirclePostComment fromDTO(CirclePostCommentOperationDTO dto) {
//        CirclePostComment comment = new CirclePostComment();
//        comment.setPostId(dto.getPostId());
//        comment.setContent(dto.getContent());
//        // 如果 DTO 还有其他字段，也复制
//        return comment;
//    }

    /**
     * 获取转义后的评论内容（防止 XSS）
     */
    public String getContent() {
        if (content != null) {
            return HtmlUtil.unescape(content);
        }
        return content;
    }
}