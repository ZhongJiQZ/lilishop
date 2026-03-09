package cn.lili.controller.promotion;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.promotion.entity.dos.PointsGoods;
import cn.lili.modules.promotion.entity.dos.PointsGoodsCategory;
import cn.lili.modules.promotion.entity.dto.search.PointsGoodsSearchParams;
import cn.lili.modules.promotion.entity.enums.PromotionsStatusEnum;
import cn.lili.modules.promotion.entity.vos.PointsGoodsVO;
import cn.lili.modules.promotion.service.PointsGoodsCategoryService;
import cn.lili.modules.promotion.service.PointsGoodsService;
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
 * 买家端,积分商品接口
 *
 * @author paulG
 * @since 2021/1/19
 **/
@RestController
@Tag(name = "买家端,积分商品接口")
@RequestMapping("/buyer/promotion/pointsGoods")
public class PointsGoodsBuyerController {
    @Autowired
    private PointsGoodsService pointsGoodsService;
    @Autowired
    private PointsGoodsCategoryService pointsGoodsCategoryService;

    @GetMapping
    @Operation(summary = "分页获取积分商品")
    public ResultMessage<IPage<PointsGoods>> getPointsGoodsPage(PointsGoodsSearchParams searchParams, PageVO page) {
        searchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        IPage<PointsGoods> pointsGoodsByPage = pointsGoodsService.pageFindAll(searchParams, page);
        return ResultUtil.data(pointsGoodsByPage);
    }

    @GetMapping("/category")
    @Operation(summary = "获取积分商品分类分页")
    public ResultMessage<IPage<PointsGoodsCategory>> page(String name, PageVO page) {
        return ResultUtil.data(pointsGoodsCategoryService.getCategoryByPage(name, page));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取积分活动商品")
    @Parameter(name = "id", description = "积分商品ID", required = true)
    public ResultMessage<PointsGoodsVO> getPointsGoodsPage(@PathVariable String id) {
        return ResultUtil.data(pointsGoodsService.getPointsGoodsDetail(id));
    }

}
