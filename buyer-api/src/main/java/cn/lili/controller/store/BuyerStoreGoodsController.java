package cn.lili.controller.store;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.entity.dto.GoodsSearchParams;
import cn.lili.modules.goods.service.GoodsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import java.util.Collections;

/**
 * 买家端-商户工作台商品接口
 */
@Tag(name = "买家端-商户工作台商品接口", description = "buyer登录体系下的商户商品管理接口（模板引用、查询、上架、下架）")
@RestController
@RequestMapping("/buyer/store/goods")
public class BuyerStoreGoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private BuyerMerchantStoreSupport buyerMerchantStoreSupport;

    @Operation(
            summary = "获取template商品列表",
            description = "返回模板店铺商品分页列表。用于商户选择模板商品进行复制。"
    )
    @Parameters({
            @Parameter(name = "pageNumber", description = "页码，从1开始", example = "1"),
            @Parameter(name = "pageSize", description = "每页数量", example = "10"),
            @Parameter(name = "goodsName", description = "商品名称（模糊搜索）", example = "iPhone"),
            @Parameter(name = "marketEnable", description = "上下架状态：UPPER/DOWN", example = "UPPER")
    })
    @GetMapping("/template/list")
    public ResultMessage<IPage<Goods>> getTemplateGoodsList(GoodsSearchParams goodsSearchParams) {
        goodsSearchParams.setStoreId(goodsService.getTemplateStoreId());
        return ResultUtil.data(goodsService.queryByParams(goodsSearchParams));
    }

    @Operation(
            summary = "获取当前商户商品列表",
            description = "返回当前登录商户店铺下的商品分页列表。"
    )
    @Parameters({
            @Parameter(name = "pageNumber", description = "页码，从1开始", example = "1"),
            @Parameter(name = "pageSize", description = "每页数量", example = "10"),
            @Parameter(name = "goodsName", description = "商品名称（模糊搜索）", example = "iPhone"),
            @Parameter(name = "marketEnable", description = "上下架状态：UPPER/DOWN", example = "DOWN")
    })
    @GetMapping("/mine/list")
    public ResultMessage<IPage<Goods>> getMineGoodsList(GoodsSearchParams goodsSearchParams) {
        goodsSearchParams.setStoreId(getCurrentStoreId());
        return ResultUtil.data(goodsService.queryByParams(goodsSearchParams));
    }

    @Operation(
            summary = "复制template商品并上架",
            description = "根据模板商品ID复制最小必要字段到当前商户店铺，并默认按模板状态上架/下架。"
    )
    @Parameter(name = "templateGoodsId", description = "模板商品ID", required = true, example = "1376521743522332672")
    @PostMapping("/copy-template/{templateGoodsId}")
    public ResultMessage<Object> copyTemplateGoods(@NotBlank @PathVariable String templateGoodsId) {
        goodsService.copyMinimalGoodsFromTemplate(templateGoodsId, getCurrentStoreId());
        return ResultUtil.success();
    }

    @Operation(
            summary = "下架当前商户单个商品",
            description = "将当前商户店铺下指定商品下架。仅允许操作本店商品。"
    )
    @Parameter(name = "goodsId", description = "商品ID", required = true, example = "1376521743522332672")
    @PostMapping("/under/{goodsId}")
    public ResultMessage<Object> underMineGoods(@NotBlank @PathVariable String goodsId) {
        goodsService.underGoodsByStore(Collections.singletonList(goodsId), getCurrentStoreId(), "商户手动下架");
        return ResultUtil.success();
    }

    @Operation(
            summary = "上架当前商户单个商品",
            description = "将当前商户店铺下指定商品上架。仅允许操作本店商品。"
    )
    @Parameter(name = "goodsId", description = "商品ID", required = true, example = "1376521743522332672")
    @PostMapping("/upper/{goodsId}")
    public ResultMessage<Object> upperMineGoods(@NotBlank @PathVariable String goodsId) {
        goodsService.upperGoodsByStore(Collections.singletonList(goodsId), getCurrentStoreId(), "商户手动上架");
        return ResultUtil.success();
    }

    private String getCurrentStoreId() {
        return buyerMerchantStoreSupport.requireCurrentStoreId();
    }
}
