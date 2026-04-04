package cn.lili.common.utils;

import cn.lili.modules.eid.entity.dos.MemberEidRecord;
import cn.lili.modules.eid.service.MemberEidRecordService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.faceid.v20180301.FaceidClient;
import com.tencentcloudapi.faceid.v20180301.models.*;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 腾讯云E证通工具类
 */
@Slf4j
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

    @Resource
    private MemberEidRecordService memberEidRecordService;

    /**
     * 获取E证通Token（认证链接）
     */
    public EidTokenResult getEidToken(String name, String idCard) {
        try {
            log.info("secretId = {}, secretKey = {}", secretId, secretKey);
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

    /**
     * 第三步：查询核身结果 + 自动保存到数据库
     */
    public GetEidResultResponse getEidResultAndSave(String eidToken, String memberId) {
        try {
            FaceidClient client = getFaceidClient();
            GetEidResultRequest req = new GetEidResultRequest();
            req.setEidToken(eidToken);
            req.setInfoType("0");

            GetEidResultResponse resp = client.GetEidResult(req);

            // ====================== 关键：保存核身结果 ======================
            saveEidRecord(eidToken, resp, memberId);

            return resp;

        } catch (TencentCloudSDKException e) {
            log.error("查询E证通结果失败", e);
            throw new RuntimeException("查询E证通结果失败：" + e.getMessage());
        }
    }

    /**
     * 保存E证通核身记录到数据库
     */
    private void saveEidRecord(String eidToken, GetEidResultResponse resp, String memberId) {
        try {
            MemberEidRecord r = new MemberEidRecord();
            r.setMemberId(memberId);
            r.setEidToken(eidToken);
            r.setCreateTime(new Date());
            r.setResultJson(resp.toString());

            // 文本结果
            DetectInfoText text = resp.getText();
            if (text != null) {
                r.setErrCode(text.getErrCode());
                r.setErrMsg(text.getErrMsg());
                r.setStatus(text.getErrCode() != null && text.getErrCode() == 0 ? "SUCCESS" : "FAIL");
                r.setName(text.getName());
                r.setIdCard(text.getIdCard());
//                r.setUseIdType(text.getUseIDType() != null ? text.getUseIDType().toString() : null);
//                r.setIdInfoFrom(text.getIdInfoFrom());

                r.setOcrNation(text.getOcrNation());
                r.setOcrAddress(text.getOcrAddress());
                r.setOcrBirth(text.getOcrBirth());
                r.setOcrAuthority(text.getOcrAuthority());
                r.setOcrValidDate(text.getOcrValidDate());
                r.setOcrGender(text.getOcrGender());

                r.setLiveStatus(text.getLiveStatus());
                r.setLiveMsg(text.getLiveMsg());
                r.setCompareStatus(text.getComparestatus());
                r.setCompareMsg(text.getComparemsg());
                r.setSim(text.getSim());
                r.setCompareLibType(text.getCompareLibType());
                r.setLivenessMode(text.getLivenessMode());

                r.setMobile(text.getMobile());

                // 攻击风险标签
//                if (text.getLivenessInfoTag() != null) {
//                    r.setLivenessInfoTag(String.join(",", text.getLivenessInfoTag()));
//                }
            }

            // 图片/最佳帧/OCR
            DetectInfoBestFrame bestFrame = resp.getBestFrame();
            if (bestFrame != null) {
                r.setBestFrame(bestFrame.getBestFrame());
            }

            DetectInfoIdCardData idCardData = resp.getIdCardData();
            if (idCardData != null) {
                r.setOcrFront(idCardData.getOcrFront());
                r.setOcrBack(idCardData.getOcrBack());
                r.setAvatar(idCardData.getAvatar());
            }

            // 意愿核身结果（任意一种模式都存）
            IntentionVerifyData intention = resp.getIntentionVerifyData();
            if (intention != null) {
                r.setFinalResultDetailCode(intention.getErrorCode() != null ? intention.getErrorCode().toString() : null);
                r.setFinalResultMessage(intention.getErrorMessage());
            }

            IntentionQuestionResult question = resp.getIntentionQuestionResult();
            if (question != null) {
                r.setFinalResultDetailCode(question.getFinalResultCode());
//                r.setFinalResultMessage(question.getFinalResultMessage());
            }

//            IntentionActionResult action = resp.getIntentionActionResult();
//            if (action != null) {
//                r.setFinalResultDetailCode(action.getFinalResultDetailCode() != null ? action.getFinalResultDetailCode().toString() : null);
//                r.setFinalResultMessage(action.getFinalResultMessage());
//            }

            memberEidRecordService.save(r);
            log.info("E证通记录保存成功 memberId={} errCode={}", memberId, r.getErrCode());

        } catch (Exception e) {
            log.error("保存E证通记录失败", e);
        }
    }
}
