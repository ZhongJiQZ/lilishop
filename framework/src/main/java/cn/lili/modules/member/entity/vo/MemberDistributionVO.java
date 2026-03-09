package cn.lili.modules.member.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员分布VO
 *
 * @author Chopper
 * @since 2021-02-26 17:25
 */
@Data
public class MemberDistributionVO {

    @Schema(description = "客户端类型")
    private String clientEnum;

    @Schema(description = "数量")
    private Integer num;

    @Schema(description = "比例")
    private Double proportion;

}
