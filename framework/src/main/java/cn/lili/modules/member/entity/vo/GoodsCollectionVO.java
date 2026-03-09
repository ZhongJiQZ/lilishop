package cn.lili.modules.member.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员商品收藏VO
 *
 * @author Chopper
 * @since 2021/1/27 10:41 上午
 */
@Data
public class GoodsCollectionVO {

    @Schema(description = "id")
    private String id;

    @Schema(description = "商品ID")
    private String goodsId;

    @Schema(description = "规格ID")
    private String skuId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品图片")
    private String image;

    @Schema(description = "商品价格")
    private Double price;

    @Schema(description = "已失效")
    private String marketEnable;
}
