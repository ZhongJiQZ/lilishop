package cn.lili.modules.wallet.entity.dos;

import cn.lili.common.security.sensitive.Sensitive;
import cn.lili.common.security.sensitive.enums.SensitiveStrategy;
import cn.lili.modules.wallet.entity.dto.MemberWalletUpdateDTO;
import cn.lili.modules.wallet.entity.enums.DepositServiceTypeEnum;
import cn.lili.mybatis.BaseEntity;
import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 预存款日志实体
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_wallet_log")
@Schema(description = "钱包变动日志")
@NoArgsConstructor
public class WalletLog extends BaseIdEntity {

    private static final long serialVersionUID = -1599270544927161096L;

    @Schema(description = "会员id")
    private String memberId;

    @Schema(description = "会员名称")
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String memberName;

    @Schema(description = "金额")
    private Double money;

    /**
     * @see DepositServiceTypeEnum
     */
    @Schema(description = "业务类型")
    private String serviceType;

    @Schema(description = "日志明细")
    private String detail;


    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者", hidden = true)
    private String createBy;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", hidden = true)
    private Date createTime;

    /**
     * 构建新的预存款日志对象
     *
     * @param memberName            会员名称
     * @param memberWalletUpdateDTO 变动模型
     */
    public WalletLog(String memberName, MemberWalletUpdateDTO memberWalletUpdateDTO) {
        this.setMemberId(memberWalletUpdateDTO.getMemberId());
        this.setMemberName(memberName);
        this.setMoney(memberWalletUpdateDTO.getMoney());
        this.setDetail(memberWalletUpdateDTO.getDetail());
        this.setServiceType(memberWalletUpdateDTO.getServiceType());
    }

    /**
     * 构建新的预存款日志对象
     *
     * @param memberName            会员名称
     * @param memberWalletUpdateDTO 变动模型
     * @param isReduce              是否是消费
     */
    public WalletLog(String memberName, MemberWalletUpdateDTO memberWalletUpdateDTO, boolean isReduce) {
        this.setMemberId(memberWalletUpdateDTO.getMemberId());
        this.setMemberName(memberName);
        this.setMoney(isReduce ? -memberWalletUpdateDTO.getMoney() : memberWalletUpdateDTO.getMoney());
        this.setDetail(memberWalletUpdateDTO.getDetail());
        this.setServiceType(memberWalletUpdateDTO.getServiceType());
    }

}