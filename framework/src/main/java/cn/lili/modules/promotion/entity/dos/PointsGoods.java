package cn.lili.modules.promotion.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 积分商品实体类
 *
 * @author paulG
 * @since 2020-03-19 10:44 上午
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_points_goods")
@Schema(description = "积分商品")
@AllArgsConstructor
@NoArgsConstructor
public class PointsGoods extends BasePromotions {

    private static final long serialVersionUID = 1313207311581661571L;

    @Schema(description = "商品编号")
    private String goodsId;

    @Schema(description = "商品sku编号")
    private String skuId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品原价")
    private Double originalPrice;

    @Schema(description = "结算价格")
    private Double settlementPrice;

    @Schema(description = "积分商品分类编号")
    private String pointsGoodsCategoryId;

    @Schema(description = "分类名称")
    private String pointsGoodsCategoryName;

    @Schema(description = "缩略图")
    private String thumbnail;

    @Schema(description = "活动库存数量")
    private Integer activeStock;

    @Schema(description = "兑换积分")
    private Long points;

}
