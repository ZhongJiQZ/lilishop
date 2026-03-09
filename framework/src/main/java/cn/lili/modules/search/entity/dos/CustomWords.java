package cn.lili.modules.search.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 自定义分词
 *
 * @author paulG
 * @since 2020/10/15
 **/
@Data
@TableName("li_custom_words")
@Schema(description = "自定义分词")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomWords extends BaseEntity {

    private static final long serialVersionUID = 650889506808657977L;

    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotEmpty(message = "分词名称必填")
    @Length(max = 20, message = "分词名称长度不能大于20")
    private String name;


    @Schema(description = "是否禁用: 0,禁用;1,不禁用")
    private Integer disabled;


    public CustomWords(String name) {
        this.name = name;
        this.disabled = 1;
    }
}
