package cn.lili.modules.wallet.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 会员预存款
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_member_wallet")
@Schema(description = "会员预存款")
public class MemberWallet extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会员用户名")
    private String memberName;

    @Schema(description = "会员ID")
    private String memberId;

    @Schema(description = "会员预存款")
    private Double memberWallet;

    @Schema(description = "会员预存款冻结金额,提现使用")
    private Double memberFrozenWallet;

    @Schema(description = "预存款密码")
    private String walletPassword;

}
