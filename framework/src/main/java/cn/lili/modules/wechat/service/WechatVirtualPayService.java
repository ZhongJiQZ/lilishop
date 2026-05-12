package cn.lili.modules.wechat.service;

import cn.lili.modules.wallet.entity.dos.Recharge;
import java.util.Map;

public interface WechatVirtualPayService {

    /**
     * 获取小程序虚拟支付拉起参数
     * @param recharge 充值订单
     * @return 前端所需 paySig / signature / signData / mode 等
     */
    Map<String, Object> getPayParams(Recharge recharge);

    /**
     * 处理微信虚拟支付发货回调
     * @param outTradeNo 业务订单号
     * @param transactionId 微信交易单号
     */
    void handleNotify(String outTradeNo, String transactionId);
}
