package cn.lili.controller.store;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.entity.dto.GoodsSearchParams;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.service.StoreService;
import cn.lili.modules.goods.service.GoodsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

/**
 * 买家端-商户工作台商品接口
 */
@Tag(name = "买家端-商户工作台商品接口")
@RestController
@RequestMapping("/buyer/store/goods")
public class BuyerStoreGoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StoreService storeService;

    @Operation(summary = "获取template商品列表")
    @GetMapping("/template/list")
    public ResultMessage<IPage<Goods>> getTemplateGoodsList(GoodsSearchParams goodsSearchParams) {
        goodsSearchParams.setStoreId(goodsService.getTemplateStoreId());
        return ResultUtil.data(goodsService.queryByParams(goodsSearchParams));
    }

    @Operation(summary = "获取当前商户商品列表")
    @GetMapping("/mine/list")
    public ResultMessage<IPage<Goods>> getMineGoodsList(GoodsSearchParams goodsSearchParams) {
        goodsSearchParams.setStoreId(getCurrentStoreId());
        return ResultUtil.data(goodsService.queryByParams(goodsSearchParams));
    }

    @Operation(summary = "复制template商品并上架")
    @PostMapping("/copy-template/{templateGoodsId}")
    public ResultMessage<Object> copyTemplateGoods(@NotBlank @PathVariable String templateGoodsId) {
        goodsService.copyMinimalGoodsFromTemplate(templateGoodsId, getCurrentStoreId());
        return ResultUtil.success();
    }

    @Operation(summary = "下架当前商户商品")
    @PutMapping("/under")
    public ResultMessage<Object> underMineGoods(@RequestBody List<String> goodsIds) {
        goodsService.underGoodsByStore(goodsIds, getCurrentStoreId(), "商户手动下架");
        return ResultUtil.success();
    }

    private String getCurrentStoreId() {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        if (CharSequenceUtil.isNotEmpty(currentUser.getStoreId())) {
            return currentUser.getStoreId();
        }
        Store store = storeService.getOne(new LambdaQueryWrapper<Store>().eq(Store::getMemberId, currentUser.getId()), false);
        if (store == null) {
            throw new ServiceException("当前账号未绑定店铺");
        }
        return store.getId();
    }
}
