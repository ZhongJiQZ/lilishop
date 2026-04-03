package cn.lili.modules.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 邀请佣金设置
 *
 * @author lensing
 * @since 2026/04/03 7:59 下午
 */
@Data
public class InviteCommissionSetting implements Serializable {

    @Schema(description = "是否开启")
    private Boolean isOpen;

    @Schema(description = "佣金比例 0.10=10%")
    private BigDecimal commissionRate;

}
