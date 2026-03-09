package cn.lili.modules.order.cart.entity.vo;

import cn.lili.modules.order.order.entity.dto.PriceDetailDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单价格详情
 *
 * @author Chopper
 * @since 2020-04-01 10:42 上午
 */
@Data
public class PriceDetailVO implements Serializable {

    private static final long serialVersionUID = -960537582096338500L;

    @Schema(description = "商品原价")
    private Double originalPrice;

    @Schema(description = "配送费")
    private Double freight;

    @Schema(description = "优惠金额")
    private Double discountPrice;

    @Schema(description = "支付积分")
    private Long payPoint;

    @Schema(description = "最终成交金额")
    private Double finalePrice;


    /**
     * 构造器，初始化默认值
     */
    public PriceDetailVO(PriceDetailDTO dto) {
        this.freight = dto.getFreightPrice();
        this.finalePrice = dto.getFlowPrice();
        this.discountPrice = dto.getDiscountPrice();
        this.payPoint = dto.getPayPoint();
        this.originalPrice = dto.getGoodsPrice();
    }

    public PriceDetailVO() {

    }
}
