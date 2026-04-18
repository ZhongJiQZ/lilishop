package cn.lili.event.impl;

import cn.lili.event.OrderStatusChangeEvent;
import cn.lili.modules.order.order.entity.dto.OrderMessage;
import cn.lili.modules.order.order.service.OrderService;
import cn.lili.modules.wechat.service.WechatMPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信小程序执行器
 *
 * @author Chopper
 * @version v1.0 2021-04-19 14:25
 */
@Slf4j
@Service
public class WechatMPExecute implements OrderStatusChangeEvent {

    @Autowired
    private WechatMPService wechatMPService;

    @Autowired
    private OrderService orderService;


    /**
     * 订单已发货、待提货、待核验状态 如果是微信小程序的订单则进行 订单发货信息录入
     *
     * @param orderMessage 订单消息
     */
    @Override
    public void orderChange(OrderMessage orderMessage) {

        switch (orderMessage.getNewStatus()) {
            case TAKE:
            case STAY_PICKED_UP:
            case DELIVERED:
                try {
                    wechatMPService.uploadShippingInfo(orderMessage.getOrderSn());
                    // 2. 成功：更新为“已上报”
                    orderService.updateWxUploadShipping(orderMessage.getOrderSn(), 1);
                } catch (Exception e) {
                    log.error("发货信息录入失败", e);
                    // 2. 失败：更新为“上报失败”
                    orderService.updateWxUploadShipping(orderMessage.getOrderSn(), 2);
                }
                break;
            case COMPLETED:
                try {
                    wechatMPService.notifyConfirmReceive(orderMessage.getOrderSn());
                    // 2. 成功：更新为“已上报”
                    orderService.updateWxConfirmReport(orderMessage.getOrderSn(), 1);
                } catch (Exception e) {
                    log.error("微信消息发送失败", e);
                    // 2. 失败：更新为“上报失败”
                    orderService.updateWxConfirmReport(orderMessage.getOrderSn(), 2);
                }
                break;
            default:
                break;
        }
    }


}
