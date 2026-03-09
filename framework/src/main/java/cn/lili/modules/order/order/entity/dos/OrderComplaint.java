package cn.lili.modules.order.order.entity.dos;

import cn.lili.common.security.sensitive.Sensitive;
import cn.lili.common.security.sensitive.enums.SensitiveStrategy;
import cn.lili.modules.order.aftersale.entity.enums.ComplaintStatusEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 订单交易投诉
 *
 * @author paulG
 * @since 2020/12/4
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_order_complaint")
@Schema(description = "订单交易投诉")
public class OrderComplaint extends BaseEntity {

    private static final long serialVersionUID = 7185050229757228184L;


    @Schema(description = "投诉主题")
    private String complainTopic;

    @Schema(description = "投诉内容")
    private String content;

    @Schema(description = "投诉凭证图片")
    private String images;

    /**
     * @see ComplaintStatusEnum
     */
    @Schema(description = "交易投诉状态")
    private String complainStatus;

    @Schema(description = "申诉商家内容")
    private String appealContent;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "申诉商家时间")
    private Date appealTime;

    @Schema(description = "申诉商家上传的图片")
    private String appealImages;

    @Schema(description = "订单号")
    private String orderSn;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "下单时间")
    private Date orderTime;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品id")
    private String goodsId;

    @Schema(description = "sku主键")
    private String skuId;

    @Schema(description = "商品价格")
    private Double goodsPrice;

    @Schema(description = "商品图片")
    private String goodsImage;

    @Schema(description = "购买的商品数量")
    private Integer num;

    @Schema(description = "运费")
    private Double freightPrice;

    @Schema(description = "订单金额")
    private Double orderPrice;

    @Schema(description = "物流单号")
    private String logisticsNo;

    @Schema(description = "商家id")
    private String storeId;

    @Schema(description = "商家名称")
    private String storeName;

    @Schema(description = "会员id")
    private String memberId;

    @Schema(description = "会员名称")
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String memberName;

    @Schema(description = "收货人")
    private String consigneeName;

    @Schema(description = "收货地址")
    private String consigneeAddressPath;

    @Schema(description = "收货人手机")
    private String consigneeMobile;

    @Schema(description = "仲裁结果")
    private String arbitrationResult;


}
