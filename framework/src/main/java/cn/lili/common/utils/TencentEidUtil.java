package cn.lili.common.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.faceid.v20180301.FaceidClient;
import com.tencentcloudapi.faceid.v20180301.models.GetEidResultRequest;
import com.tencentcloudapi.faceid.v20180301.models.GetEidResultResponse;
import com.tencentcloudapi.faceid.v20180301.models.GetEidTokenConfig;
import com.tencentcloudapi.faceid.v20180301.models.GetEidTokenRequest;
import com.tencentcloudapi.faceid.v20180301.models.GetEidTokenResponse;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 腾讯云E证通工具类
 */
@Component
@ConfigurationProperties(prefix = "tencent.eid") // 从yml读取
@Data
public class TencentEidUtil {

    /**
     * 腾讯云SecretId
     */
    private String secretId;

    /**
     * 腾讯云SecretKey
     */
    private String secretKey;

    /**
     * E证通商户ID
     */
    private String merchantId;

    /**
     * 地域
     */
    private String region;

    /**
     * 获取E证通Token（认证链接）
     */
    public EidTokenResult getEidToken(String name, String idCard) {
        try {
            FaceidClient client = getFaceidClient();
            GetEidTokenRequest req = new GetEidTokenRequest();

            req.setMerchantId(merchantId);
            req.setName(name);
            req.setIdCard(idCard);

            GetEidTokenConfig config = new GetEidTokenConfig();
            config.setInputType("3");
            req.setConfig(config);

            GetEidTokenResponse resp = client.GetEidToken(req);
            EidTokenResult result = new EidTokenResult();
            result.setEidToken(resp.getEidToken());
            result.setUrl(resp.getUrl());
            result.setRequestId(resp.getRequestId());
            return result;

        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("获取E证通Token失败：" + e.getMessage());
        }
    }

    /**
     * 查询E证通认证结果
     */
    public GetEidResultResponse getEidResult(String eidToken) {
        try {
            FaceidClient client = getFaceidClient();
            GetEidResultRequest req = new GetEidResultRequest();
            req.setEidToken(eidToken);
            req.setInfoType("0");
            return client.GetEidResult(req);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("查询E证通结果失败：" + e.getMessage());
        }
    }

    /**
     * 初始化人脸识别客户端
     */
    private FaceidClient getFaceidClient() {
        Credential cred = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("faceid.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new FaceidClient(cred, region, clientProfile);
    }

    /**
     * Token返回结果
     */
    @Data
    public static class EidTokenResult {
        private String eidToken;
        private String url;
        private String requestId;
    }
}
