package cn.lili.modules.goods.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 商品规格VO
 *
 * @author paulG
 * @since 2020-02-26 23:24:13
 */
@Data
public class GoodsSkuSpecVO {


    @Schema(description = "商品skuId")
    private String skuId;

    @Schema(description = "商品sku所包含规格")
    private List<SpecValueVO> specValues;

    @Schema(description = "库存")
    private Integer quantity;

}
