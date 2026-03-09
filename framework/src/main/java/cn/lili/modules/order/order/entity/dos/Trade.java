package cn.lili.modules.order.order.entity.dos;

import cn.lili.common.utils.BeanUtil;
import cn.lili.modules.order.cart.entity.dto.TradeDTO;
import cn.lili.modules.order.cart.entity.enums.DeliveryMethodEnum;
import cn.lili.modules.order.order.entity.enums.PayStatusEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 交易
 *
 * @author Chopper
 * @since 2020/11/17 7:34 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_trade")
@Schema(description = "交易")
@NoArgsConstructor
public class Trade extends BaseEntity {

    private static final long serialVersionUID = 5177608752643561827L;

    @Schema(description = "交易编号")
    private String sn;

    @Schema(description = "买家id")
    private String memberId;

    @Schema(description = "买家用户名")
    private String memberName;

    /**
     * @see PayStatusEnum
     */
    @Schema(description = "支付方式")
    private String paymentMethod;

    /**
     * @see cn.lili.modules.order.order.entity.enums.PayStatusEnum
     */
    @Schema(description = "付款状态")
    private String payStatus;

    @Schema(description = "总价格")
    private Double flowPrice;

    @Schema(description = "原价")
    private Double goodsPrice;

    @Schema(description = "运费")
    private Double freightPrice;

    @Schema(description = "优惠的金额")
    private Double discountPrice;

    /**
     * @see DeliveryMethodEnum
     */
    @Schema(description = "配送方式")
    private String deliveryMethod;

    @Schema(description = "收货人姓名")
    private String consigneeName;

    @Schema(description = "收件人手机")
    private String consigneeMobile;

    @Schema(description = "地址名称， '，'分割")
    private String consigneeAddressPath;

    @Schema(description = "地址id，'，'分割 ")
    private String consigneeAddressIdPath;

    public Trade(TradeDTO tradeDTO) {
        String originId = this.getId();
        if (tradeDTO.getMemberAddress() != null) {
            BeanUtil.copyProperties(tradeDTO.getMemberAddress(), this);
            this.setConsigneeMobile(tradeDTO.getMemberAddress().getMobile());
            this.setConsigneeName(tradeDTO.getMemberAddress().getName());
        }
        BeanUtil.copyProperties(tradeDTO, this);
        BeanUtil.copyProperties(tradeDTO.getPriceDetailDTO(), this);
        this.setPayStatus(PayStatusEnum.UNPAID.name());
        this.setId(originId);
    }
}