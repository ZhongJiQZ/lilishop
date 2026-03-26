package cn.lili.controller.circle;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.circle.entity.dto.CirclePostCommentOperationDTO;
import cn.lili.modules.circle.entity.dto.CirclePostOperationDTO;
import cn.lili.modules.circle.entity.dto.CirclePostSearchParams;
import cn.lili.modules.circle.entity.vos.CirclePostVO;
import cn.lili.modules.circle.service.CirclePostCommentService;
import cn.lili.modules.circle.service.CirclePostFollowService;
import cn.lili.modules.circle.service.CirclePostService;
import cn.lili.modules.goods.entity.dto.GoodsOperationDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 买家端,圈子帖子接口
 *
 * @author Chopper
 * @since 2020/11/16 10:05 下午
 */
@RestController
@Tag(name = "买家端,圈子帖子接口")
@RequestMapping("/buyer/circle/post")
public class CirclePostBuyerController {
    /**
     * 圈子帖子
     */
    @Autowired
    private CirclePostService circlePostService;
    @Autowired
    private CirclePostCommentService commentService;
    @Autowired
    private CirclePostFollowService followService;

    @Operation(summary = "获取圈子帖子列表")
    @GetMapping
    public ResultMessage<IPage<CirclePostVO>> getByPage(CirclePostSearchParams circlePostSearchParams) {
        return ResultUtil.data(circlePostService.queryByParams(circlePostSearchParams));
    }

    @Operation(summary = "发布圈子帖子")
    @PostMapping(value = "/create")
    public ResultMessage<GoodsOperationDTO> save(@Valid @RequestBody CirclePostOperationDTO circlePostOperationDTO) {
        circlePostService.addCirclePost(circlePostOperationDTO);
        return ResultUtil.success();
    }

    @Operation(summary = "添加评论")
    @PostMapping(value = "/comment")
    public ResultMessage<GoodsOperationDTO> addCirclePostComment(@Valid @RequestBody CirclePostCommentOperationDTO commentOperationDTO) {
        commentService.addCirclePostComment(commentOperationDTO);
        return ResultUtil.success();
    }

    @Operation(summary = "删除圈子帖子")
    @Parameter(name = "ids", description = "圈子帖子ID", required = true)
    @DeleteMapping("/delete/{ids}")
    public ResultMessage<Object> delete(@PathVariable List<String> ids) {
        circlePostService.deleteCirclePosts(ids);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @Operation(summary = "店铺关注")
    @PostMapping(value = "/{storeId}/follow")
    public ResultMessage<Object> addStoreFollow(@PathVariable String storeId) {
        followService.addStoreFollow(storeId);
        return ResultUtil.success();
    }

    @Operation(summary = "取消店铺关注")
    @DeleteMapping(value = "/{storeId}/follow")
    public ResultMessage<Object> unfollowStore(@PathVariable String storeId) {
        followService.unfollowStore(storeId);
        return ResultUtil.success();
    }
}
