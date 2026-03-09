package cn.lili.modules.statistics.entity.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 营业构成VO
 *
 * @author Bulbasaur
 * @since 2025/08/25 7:07 下午
 */
@Data
public class BusinessCompositionDataVO {

    //订单分类构成
    @Schema(description = "到店自提")
    private Double storeSelf;
    @Schema(description = "快递发货")
    private Double express;
    @Schema(description = "线上无需配送")
    private Double online;

    //营业收入
    @Schema(description = "商品销售")
    private Double income;
    @Schema(description = "运费")
    private Double freight;
    @Schema(description = "商品返现（分销返佣）")
    private Double incomeBack;
    @Schema(description = "商品销售+费用构成")
    private Double incomeComposition;

    //退款统计
    @Schema(description = "退款订单笔数")
    private Long refundOrderNum;
    @Schema(description = "退款金额")
    private Double refund;
    @Schema(description = "退款率")
    private Double refundRate;

    //消费指标
    @Schema(description = "支付金额")
    private Double pay;
    @Schema(description = "折后笔单价")
    private Double price;
    @Schema(description = "支付人数")
    private Long payNum;
    @Schema(description = "折后客单价")
    private Double priceNum;


}
