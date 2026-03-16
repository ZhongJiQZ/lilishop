package cn.lili.controller.circle;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.circle.entity.dos.CirclePost;
import cn.lili.modules.circle.entity.dto.CirclePostPageDTO;
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
 * 管理端,圈子帖子管理接口
 *
 * @author lensing
 * @since 2026-03-16 15:18:56
 */
@RestController
@Tag(name = "管理端,圈子帖子管理接口")
@RequestMapping("/manager/circle/post")
public class CirclePostManagerController {
    /**
     * 圈子帖子
     */
    @Autowired
    private CirclePostService circlePostService;

    @Operation(summary = "分页获取")
    @Parameter(name = "circlePostSearchParams", description = "圈子帖子查询参数")
    @GetMapping("/list")
    public ResultMessage<IPage<CirclePost>> getByPage(CirclePostPageDTO page) {
        return ResultUtil.data(circlePostService.queryCirclePostByParams(page));
    }

    @Operation(summary = "获取圈子帖子")
    @Parameter(name = "id", description = "圈子帖子ID", required = true)
    @GetMapping("/get/{id}")
    public ResultMessage<CirclePost> getById(@NotNull @PathVariable String id) {
        return ResultUtil.data(circlePostService.getById(id));
    }

    @Operation(summary = "删除圈子帖子")
    @Parameter(name = "ids", description = "圈子帖子ID", required = true)
    @DeleteMapping("/delete/{ids}")
    public ResultMessage<Object> delete(@PathVariable List<String> ids) {
        circlePostService.deleteCirclePosts(ids);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @Operation(summary = "编辑圈子帖子")
    @Parameter(name = "id", description = "圈子帖子ID", required = true)
    @PutMapping("/{id}")
    public ResultMessage<CirclePost> update(@NotNull @PathVariable String id, @Valid @RequestBody CirclePost circlePost) {
        circlePost.setId(id);
        circlePostService.updateById(circlePost);
        return ResultUtil.data(circlePost);
    }

}
