package cn.lili.modules.goods.entity.vos;

import cn.lili.modules.goods.entity.dos.Commodity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 直播商品VO
 *
 * @author Bulbasaur
 * @since 2021/5/26 6:09 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommodityVO extends Commodity {

    @Schema(description = "SKU库存")
    private Integer quantity;

    @Schema(description = "店铺名称")
    private String storeName;
}
