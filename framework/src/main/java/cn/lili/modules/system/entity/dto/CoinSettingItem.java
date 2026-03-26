package cn.lili.modules.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 平台币签到设置
 *
 * @author lensing
 * @since 2026-03-25 11:48
 */
@Data
public class CoinSettingItem implements Comparable<CoinSettingItem>, Serializable {

    @Schema(description = "签到天数")
    private Integer day;

    @Schema(description = "赠送平台币")
    private Integer coin;

    public Integer getCoin() {
        if (coin == null || coin < 0) {
            return 0;
        }
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    @Override
    public int compareTo(CoinSettingItem coinSettingItem) {
        return this.day - coinSettingItem.getDay();
    }
}
