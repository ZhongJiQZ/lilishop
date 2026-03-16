package cn.lili.modules.circle.entity.dto;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HtmlUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 圈子帖子操作 DTO
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CirclePostCommentOperationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "帖子ID", required = true)
    private String postId;

    @Schema(description = "评论内容", required = true)
    @NotEmpty(message = "评论内容不能为空")
    @Length(max = 500, message = "评论内容不能超过500个字符")
    private String content;

    /**
     * 获取转义后的内容（防止 XSS）
     */
    public String getContent() {
        if (CharSequenceUtil.isNotEmpty(content)) {
            return HtmlUtil.unescape(content);
        }
        return content;
    }

}