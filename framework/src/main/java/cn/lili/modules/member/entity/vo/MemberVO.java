package cn.lili.modules.member.entity.vo;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.common.security.sensitive.Sensitive;
import cn.lili.common.security.sensitive.enums.SensitiveStrategy;
import cn.lili.common.utils.BeanUtil;
import cn.lili.modules.member.entity.dos.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author paulG
 * @since 2021/11/8
 **/
@Data
@NoArgsConstructor
public class MemberVO implements Serializable {

    private static final long serialVersionUID = 1810890757303309436L;

    @Schema(description = "唯一标识", hidden = true)
    private String id;

    @Schema(description = "会员用户名")
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String username;

    @Schema(description = "昵称")

    private String nickName;

    @Schema(description = "会员性别,1为男，0为女")
    private Integer sex;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "会员生日")
    private Date birthday;

    @Schema(description = "会员地址ID")
    private String regionId;

    @Schema(description = "会员地址")
    private String region;

    @Schema(description = "手机号码", required = true)
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String mobile;

    @Schema(description = "积分数量")
    private Long point;

    @Schema(description = "平台币数量")
    private Long coin;

    @Schema(description = "会员预存款")
    private Double memberWallet;

    @Schema(description = "积分总数量")
    private Long totalPoint;

    @Schema(description = "平台币总数量")
    private Long totalCoin;

    @Schema(description = "会员头像")
    private String face;

    @Schema(description = "会员状态")
    private Boolean disabled;

    @Schema(description = "是否开通店铺")
    private Boolean haveStore;

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "openId")
    private String openId;

    @Schema(description = "是否是VIP会员 0=普通用户 1=会员用户")
    private Integer isVip;

    @Schema(description = "是否免人脸认证 0=否(需要认证) 1=是(无需认证)")
    private Integer noFaceAuth;

    @Schema(description = "邀请码")
    private String inviteCode;

    @Schema(description = "邀请人ID")
    private String inviterId;

    @Schema(description = "邀请人Code")
    private String inviterCode;

    @Schema(description = "邀请人名称")
    private String inviterName;

    /**
     * @see ClientTypeEnum
     */
    @Schema(description = "客户端")
    private String clientEnum;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后一次登录时间")
    private Date lastLoginDate;

    @Schema(description = "会员等级ID")
    private String gradeId;

    @Schema(description = "经验值数量")
    private Long experience;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", hidden = true)
    private Date createTime;

    public MemberVO(Member member) {
        BeanUtil.copyProperties(member, this);
    }

    public String getRegionId() {
        return getDefaultValue(regionId);
    }

    public String getRegion() {
        return getDefaultValue(region);
    }

    /**
     * JSON转换中的null 会转成 "null"
     *
     * @param value
     * @return
     */
    private String getDefaultValue(String value) {
        return (value == null || "null".equals(value)) ? "" : value;
    }
}
