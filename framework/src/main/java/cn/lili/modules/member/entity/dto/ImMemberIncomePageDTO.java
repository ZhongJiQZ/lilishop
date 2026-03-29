package cn.lili.modules.member.entity.dto;

import cn.lili.common.vo.PageVO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员打赏收益dto
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Schema(description = "会员打赏收益dto")
public class ImMemberIncomePageDTO extends PageVO {

    @Schema(description = "打赏人ID（用户）")
    private String memberId;

    @Schema(description = "打赏人名称")
    private String memberName;

}
