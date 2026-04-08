package cn.lili.modules.eid.entity.dto;

import cn.lili.common.vo.PageVO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员E证通核身记录dto
 *
 * @author lensing
 * @since 2026-03-16 15:18:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Schema(description = "会员E证通核身记录dto")
public class MemberEidRecordPageDTO extends PageVO {

    private static final long serialVersionUID = 8906820486037326039L;

    @Schema(description = "会员ID")
    private String memberId;
}
