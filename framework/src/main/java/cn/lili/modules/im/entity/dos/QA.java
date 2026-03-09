package cn.lili.modules.im.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 问题答案
 *
 * @author Chopper
 * @version v1.0
 * 2022-02-09 17:59
 */
@Data
@TableName("li_qa")
@Schema(description = "租户问答")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QA extends BaseEntity {

    @Schema(description = "租户id")
    private Integer tenantId;

    @Schema(description = "问题")
    private String question;

    @Schema(description = "答案")
    private String answer;
}
