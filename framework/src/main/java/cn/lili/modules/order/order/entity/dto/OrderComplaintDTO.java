package cn.lili.modules.order.order.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 交易投诉DTO
 *
 * @author Bulbasaur
 * @since 2021/1/7 19:39
 */
@Data
public class OrderComplaintDTO {

    @NotBlank
    @Schema(description = "投诉主题")
    private String complainTopic;

    @NotBlank
    @Schema(description = "投诉内容")
    private String content;

    @Schema(description = "投诉凭证图片")
    private String images;

    @NotBlank
    @Schema(description = "订单号")
    private String orderSn;

    @NotBlank
    @Schema(description = "商品id")
    private String goodsId;

    @NotBlank
    @Schema(description = "sku主键")
    private String skuId;
}
