package cn.lili.modules.page.entity.dos;

import cn.lili.modules.page.entity.enums.ArticleCategoryEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 文章分类
 *
 * @author pikachu
 * @author Bulbasaur
 * @since 2020/12/10 17:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_article_category")
@Schema(description = "文章分类")
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @Schema(description = "分类名称")
    @NotEmpty(message = "分类名称不能为空")
    private String articleCategoryName;

    @Schema(description = "父分类ID")
    private String parentId;

    @Schema(description = "排序")
    @Min(value = 0, message = "排序值最小0，最大9999999999")
    @Max(value = 999999999, message = "排序值最小0，最大9999999999")
    @NotNull(message = "排序值不能为空")
    private Integer sort;

    @Schema(description = "层级")
    @Min(value = 0, message = "层级最小为0")
    @Max(value = 3, message = "层级最大为3")
    private Integer level;

    /**
     * @see ArticleCategoryEnum
     */
    @Schema(description = "类型")
    private String type;

    public Integer getSort() {
        if (sort == null) {
            return 0;
        }
        return sort;
    }

    public Integer getLevel() {
        if (level == null) {
            return 1;
        }
        return level;
    }
}