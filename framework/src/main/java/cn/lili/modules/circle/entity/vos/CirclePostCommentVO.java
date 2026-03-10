package cn.lili.modules.circle.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品VO
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@Data
public class CirclePostCommentVO {

    private static final long serialVersionUID = 6377623919990713567L;

    @Schema(description = "评论人")
    private String nickName;

    @Schema(description = "评论内容")
    private String content;
}
