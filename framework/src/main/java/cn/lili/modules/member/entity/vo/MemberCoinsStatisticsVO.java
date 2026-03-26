package cn.lili.modules.member.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员平台币统计VO
 *
 * @author lensing
 * @since 2026/3/25 9:52 上午
 */
@Data
public class MemberCoinsStatisticsVO {


    @Schema(description = "历史累计发放平台币数")
    private Long totalCoin;

    @Schema(description = "未使用平台币数")
    private Long unUsedCoin;
}
