package cn.lili.modules.statistics.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分类统计VO
 *
 * @author Bulbasaur
 * @since 2020/12/10 15:42
 */
@Data
public class CategoryStatisticsDataVO {


    @Schema(description = "一级分类ID")
    private String categoryId;

    @Schema(description = "一级分类名称")
    private String categoryName;

    @Schema(description = "销售数量")
    private String num;

    @Schema(description = "销售金额")
    private Double price;
}
