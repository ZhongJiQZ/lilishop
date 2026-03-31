package cn.lili.modules.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 平台币设置
 *
 * @author lensing
 * @since 2026/03/25 7:59 下午
 */
@Data
public class CoinSetting implements Serializable {

    private static final long serialVersionUID = -4261856614779031745L;
    @Schema(description = "充值赠送多少平台币")
    private BigDecimal recharge;

    @Schema(description = "注册")
    private BigDecimal register;

    @Schema(description = "消费1元赠送多少平台币")
    private BigDecimal consumer;

    @Schema(description = "平台币付款X平台币=1元")
    private BigDecimal money;

    @Schema(description = "每日签到平台币")
    private BigDecimal signIn;

    @Schema(description = "订单评价赠送平台币")
    private BigDecimal comment;

    @Schema(description = "平台币具体设置")
    private List<CoinSettingItem> coinSettingItems = new ArrayList<>();

    public BigDecimal getRecharge() {
        if (recharge == null || recharge.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return recharge;
    }

    public BigDecimal getRegister() {
        if (register == null || register.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return register;
    }

    public BigDecimal getMoney() {
        if (money == null || money.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return money;
    }

    public BigDecimal getConsumer() {
        if (consumer == null || consumer.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return consumer;
    }

    public BigDecimal getSignIn() {
        if (signIn == null || signIn.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return signIn;
    }

    public BigDecimal getComment() {
        if (comment == null || comment.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return comment;
    }
}
