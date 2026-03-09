package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 会员发票
 *
 * @author Chopper
 * @since 2021-03-29 14:10:16
 */
@Data
@TableName("li_member_receipt")
@Schema(description = "会员发票")
@EqualsAndHashCode(callSuper = false)
public class MemberReceipt extends BaseIdEntity {

    private static final long serialVersionUID = -8210927482915675995L;

    @Schema(description = "发票抬头")
    private String receiptTitle;

    @Schema(description = "纳税人识别号")
    private String taxpayerId;

    @Schema(description = "发票内容")
    private String receiptContent;

    @Schema(description = "会员ID")
    private String memberId;

    @Schema(description = "会员名称")
    private String memberName;

    /**
     * @see cn.lili.modules.member.entity.enums.MemberReceiptEnum
     */
    @Schema(description = "发票类型")
    private String receiptType;

    @Schema(description = "是否为默认选项 0：否，1：是")
    private Integer isDefault;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "删除标志 true/false 删除/未删除", hidden = true)
    private Boolean deleteFlag;


    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", hidden = true)
    private Date createTime;


}
