package cn.lili.modules.wallet.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 会员预存款VO
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
@Data
@Schema(description = "会员预存款")
@NoArgsConstructor
@AllArgsConstructor
public class MemberWalletVO {

    @Schema(description = "会员预存款")
    private Double memberWallet;

    @Schema(description = "会员预存款冻结金额,提现使用")
    private Double memberFrozenWallet;


}
