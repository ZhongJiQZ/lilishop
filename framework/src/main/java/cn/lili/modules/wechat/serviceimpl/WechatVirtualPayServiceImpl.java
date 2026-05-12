package cn.lili.modules.wechat.serviceimpl;

import cn.hutool.core.util.StrUtil;
import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.modules.connect.entity.Connect;
import cn.lili.modules.connect.service.ConnectService;
import cn.lili.modules.member.entity.dto.ConnectQueryDTO;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.payment.WechatVirtualPaySetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import cn.lili.modules.wallet.entity.dos.Recharge;
import cn.lili.modules.wallet.service.RechargeService;
import cn.lili.modules.wechat.service.WechatVirtualPayService;
import cn.lili.modules.wechat.util.WeChatVirtualPayUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WechatVirtualPayServiceImpl implements WechatVirtualPayService {

    @Autowired
    private SettingService settingService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 注入 ConnectService 用于查询 openId
    @Autowired
    private ConnectService connectService;

    /**
     * 获取虚拟支付配置
     */
    private WechatVirtualPaySetting getVirtualPaySetting() {
        Setting setting = settingService.get(SettingEnum.WECHAT_VIRTUAL_PAYMENT.name());
        return JSON.parseObject(setting.getSettingValue(), WechatVirtualPaySetting.class);
    }

    /**
     * 【正确】从 Connect 表获取用户 openId
     */
    private String getUserOpenId() {
        AuthUser authUser = UserContext.getCurrentUser();
        String userId = authUser.getId();

        // 查询微信小程序绑定的 openId
        Connect connect = connectService.queryConnect(
                ConnectQueryDTO.builder()
                        .userId(userId)
                        .unionType(ClientTypeEnum.WECHAT_MP.name())
                        .build()
        );
        if (connect == null) {
            throw new RuntimeException("请先登录微信");
        }
        return connect.getUnionId(); // openId 存在 unionId 字段
    }

    /**
     * 获取 sessionKey（从 Redis 取，安全）
     */
    private String getCurrentUserSessionKey() {
        String openId = getUserOpenId();
        return redisTemplate.opsForValue().get("MP_SESSION_KEY:" + openId);
    }

    @Override
    public Map<String, Object> getPayParams(Recharge recharge) {
        WechatVirtualPaySetting config = getVirtualPaySetting();
        String sessionKey = getCurrentUserSessionKey();

        if (StrUtil.isBlank(sessionKey)) {
            throw new RuntimeException("登录已过期，请重新登录");
        }

        int goodsPrice = (int) (recharge.getRechargeMoney() * 100);
        String mode = "short_series_coin";
        String productId = "RECHARGE";

        // 官方 signData 结构
        JSONObject signData = new JSONObject();
        signData.put("offerId", config.getOfferId());
        signData.put("buyQuantity", 1);
        signData.put("env", config.getEnv());
        signData.put("currencyType", "CNY");
        signData.put("productId", productId);
        signData.put("goodsPrice", goodsPrice);
        signData.put("outTradeNo", recharge.getRechargeSn());
        signData.put("attach", recharge.getMemberId());

        String signDataStr = signData.toString();
        String paySig = WeChatVirtualPayUtil.calcPaySig(config.getAppKey(), signDataStr);
        String signature = WeChatVirtualPayUtil.calcSignature(sessionKey, signDataStr);

        Map<String, Object> result = new HashMap<>();
        result.put("signData", signDataStr);
        result.put("paySig", paySig);
        result.put("signature", signature);
        result.put("mode", mode);
        result.put("env", config.getEnv());
        return result;
    }

    @Override
    public void handleNotify(String outTradeNo, String transactionId) {
        rechargeService.paySuccess(outTradeNo, transactionId, "WECHAT_VIRTUAL");
    }
}
