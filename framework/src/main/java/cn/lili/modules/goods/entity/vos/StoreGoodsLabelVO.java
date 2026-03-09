package cn.lili.modules.goods.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 店铺商品分类VO
 *
 * @author Bulbasaur
 * @since 2020/12/11 16:18
 */
@Data
@NoArgsConstructor
public class StoreGoodsLabelVO {

    @Schema(description = "店铺商品分类ID")
    private String id;
    @Schema(description = "店铺商品分类名称")
    private String labelName;
    @Schema(description = "层级, 从0开始")
    private Integer level;
    @Schema(description = "店铺商品分类排序")
    private BigDecimal sortOrder;
    @Schema(description = "下级分类列表")
    private List<StoreGoodsLabelVO> children;

    public StoreGoodsLabelVO(String id, String labelName, Integer level, BigDecimal sortOrder) {
        this.id = id;
        this.labelName = labelName;
        this.level = level;
        this.sortOrder = sortOrder;
    }
}
