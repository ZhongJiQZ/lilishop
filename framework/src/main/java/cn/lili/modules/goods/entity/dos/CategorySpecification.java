package cn.lili.modules.goods.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 分类参数组关联
 *
 * @author pikachu
 * @since 2020-02-26 10:34:02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_category_specification")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商品分类规格")
public class CategorySpecification extends BaseEntity {


    private static final long serialVersionUID = -4041367493342243147L;
    /**
     * 分类id
     */
    @Schema(description = "分类id")
    private String categoryId;
    /**
     * 规格id
     */
    @Schema(description = "规格id")
    private String specificationId;
}