package cn.lili.controller.circle;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.circle.entity.dos.CirclePost;
import cn.lili.modules.circle.entity.dos.CirclePostComment;
import cn.lili.modules.circle.entity.dto.CirclePostCommentSearchParams;
import cn.lili.modules.circle.service.CirclePostCommentService;
import cn.lili.modules.circle.service.CirclePostService;
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
 * 店铺端,圈子帖子评论管理接口
 *
 * @author lensing
 * @since 2026-03-16 15:18:56
 */
@RestController
@Tag(name = "店铺端,圈子帖子评论管理接口")
@RequestMapping("/store/circle/post/comment")
public class CirclePostCommentStoreController {
    /**
     * 圈子帖子评论
     */
    @Autowired
    private CirclePostCommentService circlePostCommentService;

    @Autowired
    private CirclePostService circlePostService;

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
        AuthUser currentUser = UserContext.getCurrentUser();

        CirclePostComment comment = circlePostCommentService.getById(id);
        if (comment == null) {
            throw new ServiceException(ResultCode.CIRCLE_COMMENT_NOT_EXIST);
        }

        if (UserEnums.STORE.equals(currentUser.getRole())) {
            String storeId = currentUser.getStoreId();
            if (storeId != null) {
                CirclePost post = circlePostService.getById(comment.getPostId());
                if (post == null || !storeId.equals(post.getStoreId())) {
                    throw new ServiceException(ResultCode.CIRCLE_COMMENT_PERMISSION_DENIED);
                }
            }
        }

        return ResultUtil.data(circlePostCommentService.getById(id));
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
        // 权限校验
        circlePostCommentService.checkStorePermission(List.of(id));
        circlePostCommentService.updateById(circlePostComment);
        return ResultUtil.data(circlePostComment);
    }

    @Operation(summary = "删除圈子帖子评论")
    @Parameter(name = "ids", description = "圈子帖子评论ID", required = true)
    @DeleteMapping("/delete/{ids}")
    public ResultMessage<Object> delete(@NotNull @PathVariable List<String> ids) {
        // 权限校验
        circlePostCommentService.checkStorePermission(ids);
        circlePostCommentService.removeByIds(ids);
        return ResultUtil.success();
    }

}
