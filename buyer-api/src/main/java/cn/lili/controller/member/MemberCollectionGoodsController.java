package cn.lili.controller.member;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.service.GoodsCollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;

/**
 * 买家端,会员收藏接口
 *
 * @author Chopper
 * @since 2020/11/17 2:32 下午
 */
@RestController
@Tag(name = "买家端,会员商品收藏接口")
@RequestMapping("/buyer/member/collection")
public class MemberCollectionGoodsController {

    /**
     * 会员商品收藏
     */
    @Autowired
    private GoodsCollectionService goodsCollectionService;

    @Operation(summary = "查询会员收藏列表")
    @GetMapping("/GOODS")
    public ResultMessage<Object> goodsList(PageVO page) {
        return ResultUtil.data(goodsCollectionService.goodsCollection(page));
    }

    @Operation(summary = "添加会员收藏")
    @Parameters({
            @Parameter(name = "num", description = "值")
    })
    @PostMapping("/add/GOODS/{id}")
    public ResultMessage<Object> addGoodsCollection(@NotNull(message = "值不能为空") @PathVariable String id) {
        return ResultUtil.data(goodsCollectionService.addGoodsCollection(id));

    }

    @Operation(summary = "删除会员收藏")
    @Parameters({
            @Parameter(name = "type", description = "类型", example = "GOODS:商品,STORE:店铺"),
            @Parameter(name = "num", description = "值")
    })
    @DeleteMapping("/delete/GOODS/{id}")
    public ResultMessage<Object> deleteGoodsCollection(@NotNull(message = "值不能为空") @PathVariable String id) {
        return ResultUtil.data(goodsCollectionService.deleteGoodsCollection(id));
    }

    @Operation(summary = "查询会员是否收藏")
    @Parameters({
            @Parameter(name = "type", description = "类型", example = "GOODS:商品,STORE:店铺"),
            @Parameter(name = "id", description = "值")
    })
    @GetMapping("/isCollection/GOODS/{id}")
    public ResultMessage<Boolean> isCollection(@NotNull(message = "值不能为空") @PathVariable String id) {
        return ResultUtil.data(this.goodsCollectionService.isCollection(id));
    }
}