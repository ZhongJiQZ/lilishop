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

        // ====================== 2. 调用微信云端 get_order ======================
        JSONObject remoteResult;
        try {
            remoteResult = wechatMPService.getOrderShippingStatus(orderSn);
        } catch (Exception e) {
            log.error("微信云端订单状态查询异常，订单号:{}", orderSn, e);
            throw new ServiceException("订单状态校验失败，请稍后重试");
        }

        if (remoteResult == null || remoteResult.getIntValue("errcode") != 0) {
            String errMsg = (remoteResult != null) ? remoteResult.getString("errmsg") : "接口无响应";
            log.error("微信接口返回异常 orderSn:{}, errmsg:{}", orderSn, errMsg);
            throw new ServiceException("微信订单校验失败：" + errMsg);
        }

        JSONObject orderInfo = remoteResult.getJSONObject("order");
        if (orderInfo == null) {
            throw new ServiceException("未查询到订单微信上报信息");
        }

        // ====================== 3. 官方真实字段：order_state ======================
        int orderState = orderInfo.getIntValue("order_state");

        // 官方规则：
        // 1=待发货 2=已发货 3=确认收货 4=交易完成 5=退款 6=待结算
        boolean cloudShip = orderState == 2;       // 已发货
        boolean cloudConfirm = orderState >= 3;     // 已确认收货

        // ====================== 4. 一致性校验 ======================
        if (localWxShip != cloudShip) {
            log.error("订单发货状态不一致 本地:{} 云端:{} orderSn:{}", localWxShip, cloudShip, orderSn);
            throw new ServiceException("订单发货状态异常");
        }

        if (localWxConfirm != cloudConfirm) {
            log.error("订单收货状态不一致 本地:{} 云端:{} orderSn:{}", localWxConfirm, cloudConfirm, orderSn);
            throw new ServiceException("订单收货状态异常");
        }

        // ====================== 5. 正常返回 ======================
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
