package cn.lili.controller.wechat;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.enums.DeliverStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.order.order.service.OrderService;
import cn.lili.modules.wechat.entity.vo.OrderShippingStatusVO;
import cn.lili.modules.wechat.service.WechatMPService;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信订单发货状态接口（微信确认收货回调校验）
 */
@Slf4j
@RestController
@Tag(name = "买家端,微信订单发货状态接口")
@RequestMapping("/buyer/order/shipping")
public class OrderShippingController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WechatMPService wechatMPService;

    @Operation(summary = "查询订单发货/确认收货状态（微信回调安全校验）")
    @GetMapping("/status/{orderSn}")
    public OrderShippingStatusVO getShippingStatus(@PathVariable String orderSn) {
        AuthUser authUser = UserContext.getCurrentUser();
        if (authUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }

        Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getSn, orderSn)
                .eq(Order::getMemberId, authUser.getId()));

        if (order == null) {
            throw new ServiceException(ResultCode.ORDER_NOT_EXIST);
        }

        // ====================== 1. 本地状态 ======================
        boolean localShip = order.getDeliverStatus() != null && DeliverStatusEnum.DELIVERED.name().equals(order.getDeliverStatus());
        boolean localConfirm = OrderStatusEnum.COMPLETED.name().equals(order.getOrderStatus());
        boolean localWxShip = order.getWxUploadShipping() != null && order.getWxUploadShipping() == 1;
        boolean localWxConfirm = order.getWxConfirmReceive() != null && order.getWxConfirmReceive() == 1;

        // ====================== 2. 默认先给 false ======================
        boolean cloudShip = false;
        boolean cloudConfirm = false;

        try {
            // 调用微信接口
            JSONObject remoteResult = wechatMPService.getOrderShippingStatus(orderSn);

            // 接口正常才赋值
            if (remoteResult != null && remoteResult.getIntValue("errcode") == 0) {
                JSONObject orderInfo = remoteResult.getJSONObject("order");
                if (orderInfo != null) {
                    int orderState = orderInfo.getIntValue("order_state");
                    cloudShip = orderState == 3 || orderState == 4 || orderState == 2;
                    cloudConfirm = orderState >= 3;
                }
            }
        } catch (Exception e) {
            // 异常 → 保持 false
            log.error("微信订单状态查询异常 orderSn:{}", orderSn, e);
        }

        // ====================== 3. 不一致 → 强制设为 false ======================
        if (localWxShip != cloudShip) {
            log.error("发货状态不一致 本地:{} 云端:{} orderSn:{}", localWxShip, cloudShip, orderSn);
            cloudShip = false; // 强制false
        }

        if (localWxConfirm != cloudConfirm) {
            log.error("收货状态不一致 本地:{} 云端:{} orderSn:{}", localWxConfirm, cloudConfirm, orderSn);
            cloudConfirm = false; // 强制false
        }

        // ====================== 4. 最终返回 ======================
        OrderShippingStatusVO vo = new OrderShippingStatusVO();
        vo.setOrderSn(orderSn);
        vo.setOrderStatus(order.getOrderStatus());
        vo.setShipStatus(localShip);
        vo.setConfirmReceiveStatus(localConfirm);
        vo.setWxUploadShipping(cloudShip);
        vo.setWxConfirmReceive(cloudConfirm);

        return vo;
    }
}
