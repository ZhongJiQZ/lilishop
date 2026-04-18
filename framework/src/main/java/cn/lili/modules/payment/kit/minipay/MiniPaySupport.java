package cn.lili.modules.payment.kit.minipay;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.lili.cache.Cache;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.HttpUtils;
import cn.lili.common.utils.SnowFlake;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.connect.entity.Connect;
import cn.lili.modules.connect.entity.enums.SourceEnum;
import cn.lili.modules.connect.service.ConnectService;
import cn.lili.modules.member.entity.dto.ConnectQueryDTO;
import cn.lili.modules.payment.kit.CashierSupport;
import cn.lili.modules.payment.kit.dto.PayParam;
import cn.lili.modules.payment.kit.dto.PaymentSuccessParams;
import cn.lili.modules.payment.kit.params.dto.CashierParam;
import cn.lili.modules.payment.service.PaymentService;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.payment.WechatPaymentSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 聚合支付（微信小程序）支持。
 */
@Slf4j
@Component
public class MiniPaySupport {

    private static final String SUCCESS_STATUS = "10000";
    private static final String PAY_SUCCESS = "1";
    private static final String CACHE_KEY_PREFIX = "{mini_pay_notify_param}_";

    @Autowired
    private CashierSupport cashierSupport;
    @Autowired
    private ConnectService connectService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private Cache<String> cache;

    @Value("${lili.payment.minipay.url:https://api2.lfwin.com/payapi/mini/wxpay}")
    private String miniPayUrl;
    @Value("${lili.payment.minipay.service:comm.mini.pay}")
    private String miniPayService;
    @Value("${lili.payment.minipay.apikey:25378601}")
    private String apiKey;
    @Value("${lili.payment.minipay.signkey:84414440}")
    private String signKey;
    @Value("${lili.payment.minipay.subWxMchid:884214372}")
    private String subWxMchid;
    @Value("${lili.payment.minipay.notifyPath:/buyer/payment/cashier/miniPay/notify}")
    private String notifyPath;
    @Value("${lili.payment.minipay.notifyUrl:}")
    private String miniPayNotifyUrl;

    public Map<String, String> miniProgramPay(PayParam payParam) {
        if (StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(signKey)) {
            throw new ServiceException(ResultCode.PAY_NOT_SUPPORT);
        }
        log.info("聚合支付开始请求, orderType={}, sn={}, clientType={}",
                payParam.getOrderType(), payParam.getSn(), payParam.getClientType());
        Connect connect = connectService.queryConnect(
                ConnectQueryDTO.builder()
                        .userId(UserContext.getCurrentUser().getId())
                        .unionType(SourceEnum.WECHAT_MP_OPEN_ID.name())
                        .build()
        );
        if (connect == null || StringUtils.isEmpty(connect.getUnionId())) {
            throw new ServiceException(ResultCode.USER_CONNECT_ERROR);
        }

        WechatPaymentSetting wechatPaymentSetting = wechatPaymentSetting();
        if (StringUtils.isEmpty(wechatPaymentSetting.getMpAppId())) {
            throw new ServiceException(ResultCode.WECHAT_PAYMENT_NOT_SETTING);
        }

        CashierParam cashierParam = cashierSupport.cashierParam(payParam);
        String mchOrderId = SnowFlake.getIdStr();

        Map<String, String> requestParams = new HashMap<>(16);
        requestParams.put("service", miniPayService);
        requestParams.put("apikey", apiKey);
        requestParams.put("money", formatMoney(cashierParam.getPrice()));
        requestParams.put("nonce_str", RandomUtil.randomString(32));
        requestParams.put("sub_appid", wechatPaymentSetting.getMpAppId());
        requestParams.put("sub_openid", connect.getUnionId());
        requestParams.put("remarks", cashierParam.getDetail());
        requestParams.put("mch_orderid", mchOrderId);
        requestParams.put("notify_url", buildNotifyUrl(wechatPaymentSetting.getCallbackUrl()));
        log.info("聚合支付回调地址: {}", requestParams.get("notify_url"));
        requestParams.put("time_expire", String.valueOf(expireAtSecond()));
//        requestParams.put("sub_wx_mchid", subWxMchid);
        requestParams.put("attach", JSONUtil.toJsonStr(payParam));
        log.info("聚合支付签名前参数: {}", maskSensitive(requestParams));
        requestParams.put("sign", createSign(requestParams));
        log.info("聚合支付签名后参数: {}", maskSensitive(requestParams));

        String responseBody = HttpUtils.doPost(miniPayUrl, requestParams, StandardCharsets.UTF_8.name(),
                HttpUtils.HTTP_CONN_TIMEOUT, HttpUtils.HTTP_SOCKET_TIMEOUT);
        if (StringUtils.isEmpty(responseBody)) {
            throw new ServiceException(ResultCode.PAY_ERROR);
        }
        log.info("聚合支付预下单原始响应: {}", responseBody);
        JSONObject responseMap = JSONUtil.parseObj(responseBody);
        if (!SUCCESS_STATUS.equals(valueOf(responseMap.get("status")))) {
            log.error("聚合支付预下单失败: {}", responseBody);
            throw new ServiceException(ResultCode.PAY_ERROR);
        }
        if (!verifySign(responseMap)) {
            log.error("聚合支付预下单验签失败: {}", responseBody);
            throw new ServiceException(ResultCode.PAY_ERROR);
        }

        cache.put(CACHE_KEY_PREFIX + mchOrderId, JSONUtil.toJsonStr(payParam), 2 * 24 * 3600L);

        Map<String, String> payResult = new HashMap<>(8);
        payResult.put("appid", firstNotEmpty(valueOf(responseMap.get("appid")), valueOf(responseMap.get("appId"))));
        payResult.put("timeStamp", valueOf(responseMap.get("timeStamp")));
        payResult.put("nonceStr", valueOf(responseMap.get("nonceStr")));
        payResult.put("package", valueOf(responseMap.get("package")));
        payResult.put("signType", firstNotEmpty(valueOf(responseMap.get("signType")), valueOf(responseMap.get("sign_type"))));
        payResult.put("paySign", valueOf(responseMap.get("paySign")));
        log.info("聚合支付返回前端唤起参数: {}", maskSensitive(payResult));
        return payResult;
    }

    public String notify(HttpServletRequest request) {
        Map<String, String> notifyMap = convertParamMap(request.getParameterMap());
        log.info("聚合支付异步通知原始参数: {}", maskSensitive(notifyMap));
        if (notifyMap.isEmpty()) {
            return "success";
        }
        boolean signPass = verifySign(notifyMap);
        log.info("聚合支付异步通知验签结果: {}", signPass);
        if (!signPass) {
            log.error("聚合支付异步通知验签失败: {}", notifyMap);
            return "success";
        }
        String status = normalize(notifyMap.get("status"));
        String payStatus = normalize(notifyMap.get("paystatus"));
        // 部分通道异步回调不返回 status，paystatus=1 即可判定支付成功
        if ((StringUtils.isNotEmpty(status) && !SUCCESS_STATUS.equals(status)) || !PAY_SUCCESS.equals(payStatus)) {
            log.info("聚合支付异步通知非成功状态, status={}, paystatus={}", status, payStatus);
            return "success";
        }

        PayParam payParam = getPayParamFromNotify(notifyMap);
        if (payParam == null) {
            log.error("聚合支付异步通知未匹配到支付单: {}", notifyMap);
            return "success";
        }

        String receivableNo = firstNotEmpty(notifyMap.get("trade_no"), notifyMap.get("orderid"));
        Double payPrice = new BigDecimal(firstNotEmpty(notifyMap.get("paymoney"), "0")).doubleValue();
        log.info("聚合支付异步通知入账开始, orderType={}, sn={}, receivableNo={}, payPrice={}",
                payParam.getOrderType(), payParam.getSn(), receivableNo, payPrice);
        paymentService.success(new PaymentSuccessParams("WECHAT", receivableNo, payPrice, payParam));
        log.info("聚合支付异步通知入账完成, orderType={}, sn={}", payParam.getOrderType(), payParam.getSn());
        String mchOrderId = notifyMap.get("mch_orderid");
        if (StringUtils.isNotEmpty(mchOrderId)) {
            cache.remove(CACHE_KEY_PREFIX + mchOrderId);
            log.info("聚合支付异步通知缓存清理完成, mchOrderId={}", mchOrderId);
        }
        return "success";
    }

    private PayParam getPayParamFromNotify(Map<String, String> notifyMap) {
        String mchOrderId = notifyMap.get("mch_orderid");
        if (StringUtils.isNotEmpty(mchOrderId)) {
            String payParamJson = cache.getString(CACHE_KEY_PREFIX + mchOrderId);
            if (StringUtils.isNotEmpty(payParamJson)) {
                return JSONUtil.toBean(payParamJson, PayParam.class);
            }
        }
        String attach = notifyMap.get("attach");
        if (StringUtils.isNotEmpty(attach) && JSONUtil.isTypeJSON(attach)) {
            return JSONUtil.toBean(attach, PayParam.class);
        }
        return null;
    }

    private Map<String, String> convertParamMap(Map<String, String[]> parameterMap) {
        Map<String, String> params = new HashMap<>(parameterMap.size());
        parameterMap.forEach((k, v) -> {
            if (v != null && v.length > 0) {
                params.put(k, v[0]);
            }
        });
        return params;
    }

    private String createSign(Map<String, ?> originParam) {
        TreeMap<String, String> sortParam = new TreeMap<>();
        originParam.forEach((k, v) -> {
            if (!"sign".equals(k) && v != null) {
                sortParam.put(k, valueOf(v));
            }
        });
        StringBuilder builder = new StringBuilder();
        sortParam.forEach((k, v) -> {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(k).append("=").append(v);
        });
        builder.append("&signkey=").append(signKey);
        String signSource = builder.toString();
        String sign = DigestUtil.md5Hex(signSource);
        log.info("聚合支付签名原串: {}", maskSignSource(signSource));
        log.info("聚合支付签名结果: {}", sign);
        return sign;
    }

    private boolean verifySign(Map<String, ?> responseParam) {
        String responseSign = valueOf(responseParam.get("sign"));
        if (StringUtils.isEmpty(responseSign)) {
            return false;
        }
        String localSign = createSign(responseParam);
        return responseSign.equalsIgnoreCase(localSign);
    }

    private String formatMoney(Double money) {
        return BigDecimal.valueOf(money).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private long expireAtSecond() {
        return LocalDateTime.now().plusMinutes(15).atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    private String buildNotifyUrl(String callbackUrl) {
        String source = StringUtils.isNotEmpty(miniPayNotifyUrl) ? miniPayNotifyUrl : callbackUrl;
        if (StringUtils.isEmpty(source)) {
            throw new ServiceException(ResultCode.WECHAT_PAYMENT_NOT_SETTING);
        }
        String notifyUrl = normalizeNotifyUrl(source.trim());
        if (!source.equals(notifyUrl)) {
            log.warn("聚合支付回调地址已自动规范化, source={}, normalized={}", source, notifyUrl);
        }
        return notifyUrl;
    }

    private String normalizeNotifyUrl(String source) {
        String path = notifyPath.startsWith("/") ? notifyPath : "/" + notifyPath;
        int first = source.indexOf(path);
        if (first >= 0) {
            // source 中已包含回调路径时，裁剪为“域名 + 单份路径”
            return source.substring(0, first) + path;
        }
        if (source.endsWith("/") && path.startsWith("/")) {
            return source.substring(0, source.length() - 1) + path;
        }
        if (!source.endsWith("/") && !path.startsWith("/")) {
            return source + "/" + path;
        }
        return source + path;
    }

    private WechatPaymentSetting wechatPaymentSetting() {
        try {
            Setting systemSetting = settingService.get(SettingEnum.WECHAT_PAYMENT.name());
            return JSONUtil.toBean(systemSetting.getSettingValue(), WechatPaymentSetting.class);
        } catch (Exception e) {
            log.error("微信支付暂不支持", e);
            throw new ServiceException(ResultCode.PAY_NOT_SUPPORT);
        }
    }

    private String firstNotEmpty(String value, String defaultValue) {
        if (StringUtils.isNotEmpty(value)) {
            return value;
        }
        return defaultValue;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    private Map<String, Object> maskSensitive(Map<String, ?> source) {
        Map<String, Object> copy = new HashMap<>(source);
        maskKey(copy, "apikey");
        maskKey(copy, "signkey");
        maskKey(copy, "sign");
        maskKey(copy, "sub_openid");
        maskKey(copy, "nonce_str");
        maskKey(copy, "paySign");
        return copy;
    }

    private void maskKey(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            map.put(key, maskValue(valueOf(map.get(key))));
        }
    }

    private String maskSignSource(String signSource) {
        if (StringUtils.isEmpty(signSource)) {
            return signSource;
        }
        return signSource.replace("signkey=" + signKey, "signkey=" + maskValue(signKey));
    }

    private String maskValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        int len = value.length();
        if (len <= 6) {
            return "***";
        }
        return value.substring(0, 3) + "***" + value.substring(len - 3);
    }

    private String valueOf(Object value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }
}
