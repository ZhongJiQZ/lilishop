package cn.lili.modules.page.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文章VO
 *
 * @author Chopper
 * @since 2021-03-26 11:32
 */
@Data
public class ArticleVO {

    @Schema(description = "文章ID")
    private String id;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "分类名称")
    private String articleCategoryName;

    @Schema(description = "文章排序")
    private Integer sort;

    @Schema(description = "开启状态")
    private Boolean openStatus;
}
