package cn.lili.modules.wechat.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.connect.service.ConnectService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信内容安全检测（文本 + 图片）
 * 集成项目现有 WechatAccessTokenUtil，直接可用
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatSecurityUtil {

    @Autowired
    private WechatAccessTokenUtil wechatAccessTokenUtil;

    @Autowired
    private ConnectService connectService;

    /**
     * 文本安全检测
     */
    public void checkText(String content) {
        if (StrUtil.isBlank(content)) {
            log.info("[微信文本安全检测] 内容为空，跳过检测");
            return;
        }

        log.info("[微信文本安全检测] 开始检测，内容：{}", content);
        try {
            // 获取小程序 access_token
            String accessToken = wechatAccessTokenUtil.cgiAccessToken(ClientTypeEnum.WECHAT_MP);
            log.info("[微信文本安全检测] 获取accessToken：{}", accessToken);
            if (StrUtil.isBlank(accessToken)) {
                log.error("[微信文本安全检测] 获取accessToken失败");
                throw new ServiceException(ResultCode.WECHAT_CONNECT_NOT_SETTING);
            }

            // 调用微信文本安全接口
            String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken;
            Map<String, Object> param = new HashMap<>();
            param.put("content", content);
            log.info("[微信文本安全检测] 请求URL：{}，请求参数：{}", url, param);

            String result = HttpUtil.post(url, JSON.toJSONString(param));
            log.info("[微信文本安全检测] 微信返回结果：{}", result);

            JSONObject resJson = JSON.parseObject(result);
            Integer errcode = resJson.getInteger("errcode");

            if (errcode != null) {
                if (errcode == 87014) {
                    log.warn("[微信文本安全检测] 命中违规内容，errcode=87014，内容：{}", content);
                    throw new ServiceException(ResultCode.STORE_TEXT_RISK);
                }
                if (errcode != 0) {
                    log.error("[微信文本安全检测] 接口调用失败：{}", resJson);
                    throw new ServiceException("文本安全检测失败：" + resJson.getString("errmsg"));
                }
            }
            log.info("[微信文本安全检测] 检测通过，内容：{}", content);

        } catch (ServiceException e) {
            log.error("[微信文本安全检测] 业务异常：{}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("[微信文本安全检测] 系统异常", e);
            throw new ServiceException(ResultCode.STORE_TEXT_CHECK_ERROR);
        }
    }

    /**
     * 图片安全检测
     */
    public void checkImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            log.info("[微信图片安全检测] 图片为空，跳过检测");
            return;
        }

        log.info("[微信图片安全检测] 开始检测，文件名：{}", image.getOriginalFilename());
        try {
            String accessToken = wechatAccessTokenUtil.cgiAccessToken(ClientTypeEnum.WECHAT_MP);
            log.info("[微信图片安全检测] 获取accessToken：{}", accessToken);
            if (StrUtil.isBlank(accessToken)) {
                log.error("[微信图片安全检测] 获取accessToken失败");
                throw new ServiceException(ResultCode.WECHAT_CONNECT_NOT_SETTING);
            }

            // 调用微信图片安全接口
            String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken;
            log.info("[微信图片安全检测] 请求URL：{}", url);

            String result = HttpUtil.createPost(url)
                    .form("media", image.getInputStream(), "image.jpg")
                    .execute()
                    .body();
            log.info("[微信图片安全检测] 微信返回结果：{}", result);

            JSONObject resJson = JSON.parseObject(result);
            Integer errcode = resJson.getInteger("errcode");

            if (errcode != null) {
                if (errcode == 87014) {
                    log.warn("[微信图片安全检测] 图片命中违规内容，errcode=87014");
                    throw new ServiceException(ResultCode.STORE_IMAGE_RISK);
                }
                if (errcode != 0) {
                    log.error("[微信图片安全检测] 接口调用失败：{}", resJson);
                    throw new ServiceException("图片安全检测失败：" + resJson.getString("errmsg"));
                }
            }
            log.info("[微信图片安全检测] 图片检测通过");

        } catch (ServiceException e) {
            log.error("[微信图片安全检测] 业务异常：{}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("[微信图片安全检测] 系统异常", e);
            throw new ServiceException(ResultCode.STORE_IMAGE_CHECK_ERROR);
        }
    }

    /**
     * 单个图片URL安全检测
     */
    public void checkImageUrl(String imageUrl) {
        if (StrUtil.isBlank(imageUrl)) {
            log.info("[微信图片URL安全检测] URL为空，跳过检测");
            return;
        }

        log.info("[微信图片URL安全检测] 开始检测，URL：{}", imageUrl);
        try {
            String accessToken = wechatAccessTokenUtil.cgiAccessToken(ClientTypeEnum.WECHAT_MP);
            log.info("[微信图片URL安全检测] 获取accessToken：{}", accessToken);
            if (StrUtil.isBlank(accessToken)) {
                log.error("[微信图片URL安全检测] 获取accessToken失败");
                throw new ServiceException(ResultCode.WECHAT_CONNECT_NOT_SETTING);
            }

            // 调用微信图片检测
            String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken;
            log.info("[微信图片URL安全检测] 请求URL：{}", url);

            String result = HttpUtil.createPost(url)
                    .form("media", new URL(imageUrl), "image.jpg")
                    .execute()
                    .body();
            log.info("[微信图片URL安全检测] 微信返回结果：{}", result);

            JSONObject resJson = JSON.parseObject(result);
            Integer errcode = resJson.getInteger("errcode");

            if (errcode != null) {
                if (errcode == 87014) {
                    log.warn("[微信图片URL安全检测] 图片命中违规内容，errcode=87014，URL：{}", imageUrl);
                    throw new ServiceException(ResultCode.STORE_IMAGE_RISK, "图片包含违规内容：" + imageUrl);
                }
                if (errcode != 0) {
                    log.error("[微信图片URL安全检测] 接口调用失败 url={}, err={}", imageUrl, resJson);
                    throw new ServiceException("图片安全检测失败：" + resJson.getString("errmsg"));
                }
            }
            log.info("[微信图片URL安全检测] 图片URL检测通过，URL：{}", imageUrl);

        } catch (ServiceException e) {
            log.error("[微信图片URL安全检测] 业务异常：{}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("[微信图片URL安全检测] 系统异常，URL：{}", imageUrl, e);
            throw new ServiceException(ResultCode.STORE_IMAGE_RISK);
        }
    }

    /**
     * 逗号分隔的多图URL 批量校验
     */
    public void checkImageUrls(String imageUrls) {
        if (StrUtil.isBlank(imageUrls)) {
            log.info("[微信批量图片URL检测] 图片列表为空，跳过");
            return;
        }
        log.info("[微信批量图片URL检测] 开始批量检测，图片串：{}", imageUrls);
        // 按逗号拆分
        String[] urlArray = imageUrls.split(",");
        for (String url : urlArray) {
            checkImageUrl(url.trim());
        }
        log.info("[微信批量图片URL检测] 所有图片检测完成，总数：{}", urlArray.length);
    }
}