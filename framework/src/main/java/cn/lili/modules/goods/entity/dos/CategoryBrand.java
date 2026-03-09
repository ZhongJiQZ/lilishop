package cn.lili.modules.goods.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 分类品牌关联
 *
 * @author pikachu
 * @since 2020-03-02 09:34:02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_category_brand")
@Schema(description = "商品分类品牌关联")
@NoArgsConstructor
public class CategoryBrand extends BaseIdEntity {

    private static final long serialVersionUID = 3315719881926878L;


    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者", hidden = true)
    private String createBy;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", hidden = true)
    private Date createTime;

    /**
     * 分类id
     */
    @TableField(value = "category_id")
    @Schema(description = "分类id")
    private String categoryId;
    /**
     * 品牌id
     */
    @TableField(value = "brand_id")
    @Schema(description = "品牌id")
    private String brandId;

    public CategoryBrand(String categoryId, String brandId) {
        this.categoryId = categoryId;
        this.brandId = brandId;
    }
}