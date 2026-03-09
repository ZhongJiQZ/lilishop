package cn.lili.modules.procurement.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 采购入库明细实体
 * 记录入库关联的采购子项与本次入库数量
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_procurement_inbound_item")
public class ProcurementInboundItem extends BaseIdEntity {

    @Schema(description = "入库单ID")
    private String inboundId;

    @Schema(description = "采购订单子项ID")
    private String procurementOrderItemId;

    @Schema(description = "商品ID")
    private String goodsId;

    @Schema(description = "SKU ID")
    private String skuId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "预计入库量")
    private Integer expectedQuantity;

    @Schema(description = "实际入库量")
    private Integer inboundQuantity;
}
