package cn.lili.modules.member.entity.dos;

import cn.lili.common.security.sensitive.Sensitive;
import cn.lili.common.security.sensitive.enums.SensitiveStrategy;
import cn.lili.common.validation.Phone;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员地址
 *
 * @author Chopper
 * @since 2020-02-25 14:10:16
 */
@Data
@TableName("li_member_address")
@Schema(description = "会员地址")
@EqualsAndHashCode(callSuper = false)
public class MemberAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会员ID", hidden = true)
    private String memberId;

    @NotEmpty(message = "收货人姓名不能为空")
    @Schema(description = "收货人姓名")
    private String name;

    @Phone
    @Schema(description = "手机号码")
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String mobile;

    @NotBlank(message = "地址不能为空")
    @Schema(description = "地址名称， '，'分割")
    private String consigneeAddressPath;

    @NotBlank(message = "地址不能为空")
    @Schema(description = "地址id，'，'分割 ")
    private String consigneeAddressIdPath;

    @NotEmpty(message = "详细地址不能为空")
    @Schema(description = "详细地址")
    private String detail;

    @Schema(description = "是否为默认收货地址")
    private Boolean isDefault;

    @Schema(description = "地址别名")
    private String alias;

    @Schema(description = "经度")
    private String lon;

    @Schema(description = "纬度")
    private String lat;
}