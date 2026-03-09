package cn.lili.controller.payment;

import cn.lili.modules.payment.kit.RefundSupport;
import cn.lili.modules.payment.entity.enums.PaymentMethodEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 买家端,退款回调
 *
 * @author Chopper
 * @since 2020-12-18 16:59
 */
@Tag(name = "买家端,退款回调")
@RestController
@RequestMapping("/buyer/payment/cashierRefund")
public class CashierRefundController {

    @Autowired
    private RefundSupport refundSupport;


    @Operation(summary = "退款通知")
    @RequestMapping(value = "/notify/{paymentMethod}", method = {RequestMethod.GET, RequestMethod.POST})
    public void notify(HttpServletRequest request, @PathVariable String paymentMethod) {
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(paymentMethod);
        refundSupport.notify(paymentMethodEnum, request);
    }
}
