package cn.lili.modules.system.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 敏感词实体
 *
 * @author Bulbasaur
 * 2020-02-25 14:10:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_sensitive_words")
@Schema(description = "敏感词")
public class SensitiveWords extends BaseEntity {

    /**
     * 敏感词名称
     */
    @Schema(description = "敏感词名称")
    @NotEmpty(message = "敏感词必填")
    @Size(min = 2, max = 20)
    private String sensitiveWord;

}