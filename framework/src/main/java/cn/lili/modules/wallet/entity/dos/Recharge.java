package cn.lili.modules.wallet.entity.dos;

import cn.lili.common.security.sensitive.Sensitive;
import cn.lili.common.security.sensitive.enums.SensitiveStrategy;
import cn.lili.modules.order.order.entity.enums.PayStatusEnum;
import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 预存款充值记录
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
@Data
@TableName("li_recharge")
@Schema(description = "预存款充值记录")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Recharge extends BaseIdEntity {

    private static final long serialVersionUID = -1529240544327161096L;

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

    @Schema(description = "充值订单编号")
    private String rechargeSn;

    @Schema(description = "会员id")
    private String memberId;

    @Schema(description = "会员名称")
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String memberName;

    @NotEmpty(message = "充值金额不能为空")
    @Schema(description = "充值金额")
    private Double rechargeMoney;

    @NotEmpty(message = "充值方式，如：支付宝，微信不能为空")
    @Schema(description = "充值方式，如：支付宝，微信")
    private String rechargeWay;

    @Schema(description = "支付状态")
    private String payStatus;

    @Schema(description = "支付插件id")
    private String paymentPluginId;

    @Schema(description = "第三方流水")
    private String receivableNo;

    @Schema(description = "支付时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;


    /**
     * 构建充值账单信息
     *
     * @param rechargeSn 充值订单号
     * @param memberId   会员id
     * @param memberName 会员名称
     * @param money      充值金额
     */
    public Recharge(String rechargeSn, String memberId, String memberName, Double money) {
        this.rechargeSn = rechargeSn;
        this.memberId = memberId;
        this.memberName = memberName;
        this.rechargeMoney = money;
        this.payStatus = PayStatusEnum.UNPAID.name();

    }

}