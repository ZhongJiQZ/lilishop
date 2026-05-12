package cn.lili.modules.wechat.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class WeChatVirtualPayUtil {

    /**
     * 虚拟支付 paySig 签名
     * 算法：hmac_sha256(appKey, requestVirtualPayment&signDataStr)
     */
    public static String calcPaySig(String appKey, String signDataStr) {
        String uri = "requestVirtualPayment";
        String src = uri + "&" + signDataStr;
        return hmacSha256(appKey, src);
    }

    /**
     * 用户态 signature 签名
     * 算法：hmac_sha256(sessionKey, signDataStr)
     */
    public static String calcSignature(String sessionKey, String signDataStr) {
        return hmacSha256(sessionKey, signDataStr);
    }

    private static String hmacSha256(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(spec);
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
