package cn.lili.modules.procurement.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuditActionDTO {
    @Schema(description = "是否通过")
    private Boolean pass;
    @Schema(description = "备注")
    private String remark;
}
