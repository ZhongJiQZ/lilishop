package cn.lili.modules.procurement.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 盘点单明细实体，记录SKU在盘点时刻的库存与基本信息
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_inventory_count_item")
public class InventoryCountItem extends BaseEntity {

    @Schema(description = "盘点单ID")
    private String countId;

    @Schema(description = "商品ID")
    private String goodsId;

    @Schema(description = "SKU ID")
    private String skuId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "规格名称（多规格用/拼接）")
    private String skuName;

    @Schema(description = "商品上架状态")
    private String marketEnable;

    @Schema(description = "库存数量")
    private Integer quantity;
}
