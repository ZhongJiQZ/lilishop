package cn.lili.modules.procurement.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
/**
 * 报损单创建请求对象，用于店铺端提交新建报损单
 * @author Bulbasaur
 * @since 2025-12-18
 */
public class DamageReportCreateDTO {
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "报损日期")
    private Date damageDate;
    @Schema(description = "报损原因ID")
    private String damageReasonId;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "凭证图片")
    private String evidence;
    @Schema(description = "报损明细")
    private List<DamageReportItemDTO> items;
}
