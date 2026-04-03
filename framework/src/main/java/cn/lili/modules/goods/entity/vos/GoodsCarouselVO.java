package cn.lili.modules.goods.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "试穿员商品轮播VO")
public class GoodsCarouselVO {

    @Schema(description = "商品ID")
    private String goodsId;

    @Schema(description = "商品主图")
    private String goodsImage;

    @Schema(description = "商品价格")
    private Double price;

    @Schema(description = "试穿员名称")
    private String storeName;

}
