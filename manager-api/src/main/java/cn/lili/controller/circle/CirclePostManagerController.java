package cn.lili.controller.circle;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.circle.entity.dos.CirclePost;
import cn.lili.modules.circle.entity.dto.CirclePostPageDTO;
import cn.lili.modules.circle.service.CirclePostService;
import cn.lili.modules.goods.entity.dto.GoodsOperationDTO;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.service.StoreService;
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

    /**
     * 店铺
     */
    @Autowired
    private StoreService storeService;

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

    @Operation(summary = "发布圈子帖子")
    @PostMapping(value = "/create")
    public ResultMessage<GoodsOperationDTO> save(@Valid @RequestBody CirclePost circlePost) {
        if(circlePost.getStoreId() == null){
            throw new ServiceException(ResultCode.STORE_ID_REQUIRED);
        }
        Store store = storeService.getById(circlePost.getStoreId());
        if(store == null){
            throw new ServiceException(ResultCode.STORE_NOT_EXIST);
        }
        if(store.getMemberId() == null){
            throw new ServiceException(ResultCode.STORE_MEMBER_NOT_EXIST);
        }
        circlePost.setUserId(Long.valueOf(store.getMemberId()));
        circlePost.setUserType(UserEnums.STORE.name());
        circlePost.setTitle(CharSequenceUtil.sub(circlePost.getContent(), 0, 20));
        circlePostService.save(circlePost);
        return ResultUtil.success();
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
