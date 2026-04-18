package cn.lili.modules.wechat.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * 微信小程序 业务层
 * @author Chopper
 */
public interface WechatMPService {


    /**
     * 微信小程序-上传发货信息
     * @param orderSn
     */
    void uploadShippingInfo(String orderSn);

    /**
     * 订单确认收货
     * @param orderSn
     */
    void notifyConfirmReceive(String orderSn);

    /**
     * 查询订单发货状态
     * @param orderSn
     * @return
     */
    JSONObject getOrderShippingStatus(String orderSn);
}
