package cn.lili.modules.system.entity.dto.payment;

import lombok.Data;

/**
 * 微信虚拟支付配置（小程序充值/会员充值）
 */
@Data
public class WechatVirtualPaySetting {
    /** 小程序appId */
    private String appId;
    /** 虚拟支付后台 appKey
     * 可通过小程序MP查看：虚拟支付 -> 基本配置 -> 基础配置中的沙箱AppKey和现网AppKey。注意：记得根据env值选择不同AppKey，env = 0对应现网AppKey，env = 1对应沙箱AppKey */
    private String appKey;
    /** 米大师 offerId */
    private String offerId;
    /** 0正式 1沙箱 */
    private Integer env;
}