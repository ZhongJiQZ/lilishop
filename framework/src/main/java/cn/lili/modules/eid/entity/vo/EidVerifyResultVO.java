package cn.lili.modules.eid.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * E 证通查询认证结果（给前端的精简返回）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "E证通认证结果")
public class EidVerifyResultVO {

    @Schema(description = "是否核身成功")
    private boolean verified;

    @Schema(description = "提示信息")
    private String message;
}
