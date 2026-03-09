package cn.lili.modules.order.cart.entity.vo;

import cn.lili.modules.order.order.entity.dto.PriceDetailDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 购物车基础
 *
 * @author Chopper
 * @since 2020-03-24 10:33 上午
 */
@Data
public class CartBase implements Serializable {

    private static final long serialVersionUID = -5172752506920017597L;

    @Schema(description = "卖家id")
    private String storeId;

    @Schema(description = "卖家姓名")
    private String storeName;

    @Schema(description = "此商品价格流水计算")
    private PriceDetailDTO priceDetailDTO;

    @Schema(description = "此商品价格展示")
    private PriceDetailVO priceDetailVO;

    public CartBase() {
        priceDetailDTO = new PriceDetailDTO();
    }

    public PriceDetailVO getPriceDetailVO() {
        if (this.priceDetailDTO != null) {
            return new PriceDetailVO(priceDetailDTO);
        }
        return new PriceDetailVO();
    }
}
