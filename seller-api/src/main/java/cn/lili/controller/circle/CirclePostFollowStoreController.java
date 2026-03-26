package cn.lili.controller.circle;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.circle.entity.vos.CirclePostFollowVO;
import cn.lili.modules.store.service.StoreService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,店铺粉丝管理接口
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
@RestController
@Tag(name = "店铺端,店铺粉丝管理接口")
@RequestMapping("/store/circle/post/follow")
public class CirclePostFollowStoreController {

    /**
     * 店铺
     */
    @Autowired
    private StoreService storeService;

    @Operation(summary = "获取店铺粉丝分页列表")
    @Parameter(name = "page", description = "分页参数", required = true)
    @GetMapping("/{storeId}")
    public ResultMessage<IPage<CirclePostFollowVO>> getStoreFollows(@PathVariable String storeId, PageVO page) {
        return ResultUtil.data(storeService.getStoreFollows(storeId, page));
    }

}
