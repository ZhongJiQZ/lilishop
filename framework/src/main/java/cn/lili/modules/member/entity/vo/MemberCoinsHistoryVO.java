package cn.lili.modules.member.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员平台币VO
 *
 * @author lensing
 * @since 2026/3/25 9:52 上午
 */
@Data
public class MemberCoinsHistoryVO {

    @Schema(description = "当前会员平台币")
    private Long coin;

    @Schema(description = "累计获得平台币")
    private Long totalCoin;


    public MemberCoinsHistoryVO() {
        this.coin = 0L;
        this.totalCoin = 0L;
    }
}
