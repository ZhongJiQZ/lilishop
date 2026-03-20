package cn.lili.controller.circle;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.sensitive.SensitiveWordsFilter;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.circle.entity.dos.CirclePostComment;
import cn.lili.modules.circle.entity.dto.CirclePostCommentSearchParams;
import cn.lili.modules.circle.service.CirclePostCommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端,圈子帖子评论管理接口
 *
 * @author lensing
 * @since 2026-03-16 15:18:56
 */
@RestController
@Tag(name = "管理端,圈子帖子评论管理接口")
@RequestMapping("/manager/circle/post/comment")
public class CirclePostCommentManagerController {
    /**
     * 圈子帖子评论
     */
    @Autowired
    private CirclePostCommentService circlePostCommentService;

    @Operation(summary = "分页获取圈子帖子评论")
    @GetMapping
    public ResultMessage<IPage<CirclePostComment>> getByPage(CirclePostCommentSearchParams commentSearchParams) {
        return ResultUtil.data(circlePostCommentService.queryByParams(commentSearchParams));
    }

    @Operation(summary = "根据圈子帖子ID获取评论列表")
    @Parameter(name = "circlePostId", description = "圈子帖子ID", required = true)
    @GetMapping("/{circlePostId}")
    public ResultMessage<List<CirclePostComment>> getCommentCirclePost(@PathVariable String circlePostId) {
        return ResultUtil.data(circlePostCommentService.getCommentCirclePostList(circlePostId));
    }

    @Operation(summary = "获取圈子帖子评论")
    @Parameter(name = "id", description = "圈子帖子评论ID", required = true)
    @GetMapping("/get/{id}")
    public ResultMessage<CirclePostComment> getById(@NotNull @PathVariable String id) {
        CirclePostComment comment = circlePostCommentService.getById(id);
        comment.setContent(SensitiveWordsFilter.filter(comment.getContent()));
        return ResultUtil.data(comment);
    }

//    @Operation(summary = "添加圈子帖子评论")
//    @PostMapping
//    public ResultMessage<CirclePostComment> save(@Valid CirclePostComment circlePostComment) {
//        circlePostCommentService.save(circlePostComment);
//        return ResultUtil.data(circlePostComment);
//    }

    @Operation(summary = "编辑圈子帖子评论")
    @Parameter(name = "id", description = "圈子帖子评论ID", required = true)
    @PutMapping("/{id}")
    public ResultMessage<CirclePostComment> update(@NotNull @PathVariable String id, @Valid @RequestBody CirclePostComment circlePostComment) {
        circlePostComment.setId(id);
        circlePostCommentService.updateById(circlePostComment);
        return ResultUtil.data(circlePostComment);
    }

    @Operation(summary = "删除圈子帖子评论")
    @Parameter(name = "ids", description = "圈子帖子评论ID", required = true)
    @DeleteMapping("/delete/{ids}")
    public ResultMessage<Object> delete(@NotNull @PathVariable List<String> ids) {
        circlePostCommentService.removeByIds(ids);
        return ResultUtil.success();
    }

}
