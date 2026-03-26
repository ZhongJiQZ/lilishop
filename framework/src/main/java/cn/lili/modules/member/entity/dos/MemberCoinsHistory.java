package cn.lili.modules.member.entity.dos;


import cn.lili.common.security.sensitive.Sensitive;
import cn.lili.common.security.sensitive.enums.SensitiveStrategy;
import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 会员平台币历史
 *
 * @author lensing
 * @since 2026-03-25 14:10:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_member_coins_history")
@Schema(description = "会员平台币历史")
public class MemberCoinsHistory extends BaseIdEntity {

    private static final long serialVersionUID = 1L;

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

    @Schema(description = "会员ID")
    private String memberId;


    @Sensitive(strategy = SensitiveStrategy.PHONE)
    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "当前平台币")
    private Long coin;

    @Schema(description = "消费之前平台币")
    private Long beforeCoin;

    @Schema(description = "变动平台币")
    private Long variableCoin;

    @Schema(description = "content")
    private String content;

    /**
     * @see cn.lili.modules.member.entity.enums.CoinTypeEnum
     */
    @Schema(description = "平台币类型")
    private String coinType;

}