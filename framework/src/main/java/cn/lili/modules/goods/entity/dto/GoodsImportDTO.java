package cn.lili.modules.goods.entity.dto;

import cn.lili.modules.goods.entity.dos.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品导入DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsImportDTO {

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品卖点")
    private String sellingPoint;

    @Schema(description = "商品分类")
    private Category category;

    @Schema(description = "运费模板")
    private String template;

    @Schema(description = "计量单位")
    private String goodsUnit;

    @Schema(description = "发布状态")
    private Boolean release;

    @Schema(description = "商品图片")
    private List<String> images;
    private List<String> goodsGalleryList;

    @Schema(description = "成本价")
    private Double cost;

    @Schema(description = "销售价")
    private Double price;

    @Schema(description = "库存")
    private Integer quantity;

    @Schema(description = "重量")
    private Double weight;

    @Schema(description = "货号")
    private String sn;

    @Schema(description = "详情")
    private String intro;

    @Schema(description = "规格项")
    private String skuKey;

    @Schema(description = "规格值")
    private String skuValue;


}
