package cn.lili.modules.promotion.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 砍价活动商品实体类
 *
 * @author qiuqiu
 * @date 2020-7-1 10:44 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_kanjia_activity_goods")
@Schema(description = "砍价活动商品对象")
public class KanjiaActivityGoods extends BasePromotions {

    private static final long serialVersionUID = 6694714877345423488L;

    @Schema(description = "结算价格")
    @NotEmpty(message = "结算价格不能为空")
    private Double settlementPrice;

    @Schema(description = "商品原价")
    private Double originalPrice;

    @Schema(description = "最低购买金额")
    @NotEmpty(message = "最低购买金额不能为空")
    private Double purchasePrice;

    @Schema(description = "货品id")
    @NotEmpty(message = "货品id不能为空")
    private String goodsId;

    @Schema(description = "货品SkuId")
    @NotEmpty(message = "货品SkuId不能为空")
    private String skuId;

    @Schema(description = "货品名称")
    private String goodsName;

    @Schema(description = "缩略图")
    private String thumbnail;

    @Schema(description = "活动库存")
    @NotEmpty(message = "活动库存不能为空")
    private Integer stock;

    @Schema(description = "每人最低砍价金额")
    @NotEmpty(message = "每人最低砍价金额不能为空")
    private Double lowestPrice;

    @Schema(description = "每人最高砍价金额")
    @NotEmpty(message = "每人最高砍价金额不能为空")
    private Double highestPrice;
}