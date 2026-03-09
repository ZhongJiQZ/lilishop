package cn.lili.modules.order.aftersale.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 售后数量VO
 *
 * @author Bulbasaur
 * @since 2021/3/12 10:32 上午
 */
@Data
public class AfterSaleNumVO {

    @Schema(description = "申请中售后数量")
    private Integer applyNum;
    @Schema(description = "已通过售后数量")
    private Integer passNum;
    @Schema(description = "已拒绝售后数量")
    private Integer refuseNum;
    @Schema(description = "待卖家收货售后数量")
    private Integer buyerReturnNum;
    @Schema(description = "卖家确认收货售后数量")
    private Integer sellerConfirmNum;
    @Schema(description = "卖家终止售后售后数量")
    private Integer sellerTerminationNum;
    @Schema(description = "买家取消售后售后数量")
    private Integer buyerCancelNum;
    @Schema(description = "等待平台退款售后数量")
    private Integer waitRefundNum;
    @Schema(description = "已完成售后数量")
    private Integer completeNum;
}
