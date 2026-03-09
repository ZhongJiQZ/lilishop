package cn.lili.modules.goods.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品库存DTO
 *
 * @author paulG
 * @since 2020/12/23
 **/
@Data
public class GoodsSkuStockDTO {

    @Schema(description = "商品id")
    private String goodsId;

    @Schema(description = "商品skuId")
    private String skuId;

    @Schema(description = "库存")
    private Integer quantity;

    @Schema(description = "预警库存")
    private Integer alertQuantity;

    @Schema(description = "规格信息")
    private String simpleSpecs;

    @Schema(description = "商品编号")
    private String sn;

    @Schema(description = "商品名称")
    private String goodsName;

    /**
     * @see cn.lili.modules.goods.entity.enums.GoodsStockTypeEnum
     */
    @Schema(description = "类型")
    private String type;
}
