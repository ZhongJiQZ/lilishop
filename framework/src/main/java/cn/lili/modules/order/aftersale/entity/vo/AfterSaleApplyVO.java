package cn.lili.modules.order.aftersale.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 售后申请VO
 *
 * @author Chopper
 * @since 2021/3/12 10:33 上午
 */
@Data
public class AfterSaleApplyVO {

    @Schema(description = "申请退款金额单价")
    private Double applyRefundPrice;

    @Schema(description = "可申请数量")
    private Integer num;

    @Schema(description = "订单子项编号")
    private String orderItemSn;

    @Schema(description = "商品ID")
    private String goodsId;

    @Schema(description = "货品ID")
    private String skuId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品图片")
    private String image;

    @Schema(description = "商品价格")
    private Double goodsPrice;

    /**
     * @see cn.lili.modules.order.trade.entity.enums.AfterSaleRefundWayEnum
     */
    @Schema(description = "退款方式", allowableValues = "ORIGINAL,OFFLINE")
    private String refundWay;

    /**
     * @see cn.lili.modules.order.trade.entity.enums
     */
    @Schema(description = "账号类型", allowableValues = "ALIPAY,WECHATPAY,MEMBERWALLET,BANKTRANSFER")
    private String accountType;

    @Schema(description = "是否支持退货")
    private Boolean returnGoods;

    @Schema(description = "是否支持退款")
    private Boolean returnMoney;

    @Schema(description = "会员ID")
    private String memberId;


}
