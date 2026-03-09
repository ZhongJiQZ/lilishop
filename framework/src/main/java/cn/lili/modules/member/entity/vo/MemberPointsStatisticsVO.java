package cn.lili.modules.member.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员积分统计VO
 *
 * @author Chopper
 * @since 2021/2/25 9:52 上午
 */
@Data
public class MemberPointsStatisticsVO {


    @Schema(description = "历史累计发放积分数")
    private Long totalPoint;

    @Schema(description = "未使用积分数")
    private Long unUsedPoint;
}
