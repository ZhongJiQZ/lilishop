package cn.lili.modules.order.order.entity.dto;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.modules.order.order.entity.enums.OrderItemAfterSaleStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 订单导出DTO
 *
 * @author Bulbasaur
 * @since 2021/6/3 6:36 下午
 */
@Data
public class OrderExportDetailDTO {

    @Schema(description = "主订单编号")
    private String orderSn;
    @Schema(description = "子订单编号")
    private String orderItemSn;
    @Schema(description = "选购商品")
    private String goodsName;
    @Schema(description = "商品数量")
    private Integer num;
    @Schema(description = "商品ID")
    private String goodsId;
    @Schema(description = "商品单价")
    private Double unitPrice;
    @Schema(description = "订单应付金额")
    private Double flowPrice;
    @Schema(description = "运费")
    private Double freightPrice;
    @Schema(description = "优惠总金额")
    private Double discountPrice;
    @Schema(description = "平台")
    private Double siteMarketingCost;
    @Schema(description = "商家优惠")
    private Double storeMarketingCost;
    @Schema(description = "商家改价")
    private Double updatePrice;
    @Schema(description = "支付方式")
    private String paymentMethod;
    @Schema(description = "收件人")
    private String consigneeName;
    @Schema(description = "收件人手机")
    private String consigneeMobile;
    @Schema(description = "省")
    private String province;
    @Schema(description = "市")
    private String city;
    @Schema(description = "区")
    private String district;
    @Schema(description = "街道")
    private String street;
    @Schema(description = "详细地址")
    private String consigneeDetail;
    @Schema(description = "买家留言")
    private String remark;
    @Schema(description = "订单提交时间")
    private String createTime;
    @Schema(description = "支付完成时间")
    private String paymentTime;
    /**
     * @see ClientTypeEnum
     */
    @Schema(description = "来源")
    private String clientType;
    /**
     * @see OrderStatusEnum
     */
    @Schema(description = "订单状态")
    private String orderStatus;
    /**
     * @see OrderTypeEnum
     */
    @Schema(description = "订单类型")
    private String orderType;
    /**
     * @see OrderItemAfterSaleStatusEnum
     */
    @Schema(description = "售后状态")
    private String afterSaleStatus;
    @Schema(description = "取消原因")
    private String cancelReason;
    @Schema(description = "发货时间")
    private String logisticsTime;
    @Schema(description = "完成时间")
    private String completeTime;
    @Schema(description = "店铺")
    private String storeName;
}
