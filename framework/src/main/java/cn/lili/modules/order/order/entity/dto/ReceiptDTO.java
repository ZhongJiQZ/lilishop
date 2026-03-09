package cn.lili.modules.order.order.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 发票子内容
 *
 * @author Bulbasaur
 * @since 2020/11/28 11:44
 */
@Data
public class ReceiptDTO {

    @Schema(description = "发票ID")
    private String receiptId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "规格")
    private String specs;

    @Schema(description = "数量")
    private Integer num;

    @Schema(description = "单价")
    private Double goodPrice;

    @Schema(description = "小计")
    private Double subtotal;
}
