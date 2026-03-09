package cn.lili.modules.statistics.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 概览数据
 *
 * @author Bulbasaur
 * @since 2025/08/25 7:07 下午
 */
@Data
public class OverViewDataVO {


    @Schema(description = "营业额")
    private Double turnover;
    @Schema(description = "优惠金额")
    private Double discount;
    @Schema(description = "营业收入不含数值储值金额")
    private Double incomeNoStoreValue;
    @Schema(description = "支付订单数")
    private Long payOrderNum;
    @Schema(description = "新增充值金额")
    private Double recharge;
    @Schema(description = "使用充值金额")
    private Long rechargeUse;


}
