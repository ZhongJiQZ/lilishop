package cn.lili.modules.circle.entity.dto;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 圈子帖子查询条件
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CirclePostCommentSearchParams extends PageVO {

    private static final long serialVersionUID = 2544015852728566887L;

    @Schema(description = "圈子帖子id")
    private String postId;

    @Schema(description = "评论内容")
    private String content;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        // 统一使用 CharSequenceUtil.isNotEmpty() 处理字符串
        if (CharSequenceUtil.isNotEmpty(postId)) {
            queryWrapper.eq("post_id", postId);
        }

        if (CharSequenceUtil.isNotEmpty(content)) {
            queryWrapper.like("c.content", content);
        }

        queryWrapper.eq("c.delete_flag", false);
        return queryWrapper;
    }


}
