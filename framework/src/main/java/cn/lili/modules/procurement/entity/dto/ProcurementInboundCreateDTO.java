package cn.lili.modules.procurement.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 采购入库创建请求对象
 * 承载入库单创建时的关联采购单与入库明细
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Data
public class ProcurementInboundCreateDTO {

    @Schema(description = "采购订单ID")
    private String procurementOrderId;

    @Schema(description = "入库凭证")
    private String certificateImages;

    @Schema(description = "入库商品")
    private List<Item> items;

    @Data
    public static class Item {
        private String procurementOrderItemId;
        private String goodsId;
        private String skuId;
        private String goodsName;
        private Integer inboundQuantity;
    }
}
