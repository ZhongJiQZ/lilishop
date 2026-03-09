package cn.lili.modules.page.entity.dos;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HtmlUtil;
import cn.lili.modules.page.entity.enums.ArticleEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 文章DO
 *
 * @author Bulbasaur
 * @since 2020/12/10 17:42
 */
@Data
@TableName("li_article")
@Schema(description = "文章")
@EqualsAndHashCode(callSuper = false)
public class Article extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "文章标题")
    @NotEmpty(message = "文章标题不能为空")
    @Length(max = 30, message = "文章标题不能超过30个字符")
    private String title;

    @Schema(description = "分类id")
    @NotNull(message = "文章分类不能为空")
    private String categoryId;

    @Schema(description = "文章排序")
    private Integer sort;

    @Schema(description = "文章内容")
    @NotEmpty(message = "文章内容不能为空")
    private String content;

    @Schema(description = "状态")
    private Boolean openStatus;
    /**
     * @see ArticleEnum
     */
    @Schema(description = "类型")
    private String type;

    public String getContent() {
        if (CharSequenceUtil.isNotEmpty(content)) {
            return HtmlUtil.unescape(content);
        }
        return content;
    }

}