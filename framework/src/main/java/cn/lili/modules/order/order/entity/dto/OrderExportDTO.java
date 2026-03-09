package cn.lili.modules.order.order.entity.dto;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.modules.order.order.entity.enums.OrderItemAfterSaleStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单导出DTO
 *
 * @author Bulbasaur
 * @since 2021/6/3 6:36 下午
 */
@Data
public class OrderExportDTO {

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
    @Schema(description = "价格内容")
    private String priceDetail;
    @Schema(description = "支付方式")
    private String paymentMethod;
    @Schema(description = "收件人")
    private String consigneeName;
    @Schema(description = "收件人手机")
    private String consigneeMobile;
    @Schema(description = "收件人地址")
    private String consigneeAddressPath;
    @Schema(description = "详细地址")
    private String consigneeDetail;
    @Schema(description = "买家留言")
    private String remark;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "订单提交时间")
    private Date createTime;
    @Schema(description = "支付完成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logisticsTime;
    @Schema(description = "完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;
    @Schema(description = "店铺")
    private String storeName;
}
