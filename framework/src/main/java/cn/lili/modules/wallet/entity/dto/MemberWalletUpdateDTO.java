package cn.lili.modules.wallet.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 会员余额变动模型
 *
 * @author Chopper
 * @version v1.0
 * 2021-12-01 09:35
 */
@Data
@AllArgsConstructor
public class MemberWalletUpdateDTO {

    @Schema(description = "变动金额")
    private Double money;
    @Schema(description = "变动会员id")
    private String memberId;
    @Schema(description = "日志详情")
    private String detail;

    /**
     * @see cn.lili.modules.wallet.entity.enums.DepositServiceTypeEnum
     */
    @Schema(description = "变动业务原因")
    private String serviceType;
}
