package cn.lili.controller.store;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dto.OrderSearchParams;
import cn.lili.modules.order.order.entity.vo.OrderDetailVO;
import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
import cn.lili.modules.order.order.service.OrderPriceService;
import cn.lili.modules.order.order.service.OrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 买家端-商户工作台订单接口
 */
@Tag(name = "买家端-商户工作台订单接口", description = "buyer登录体系下的商户订单管理接口（列表、详情、改价）")
@RestController
@RequestMapping("/buyer/store/order")
public class BuyerStoreOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderPriceService orderPriceService;

    @Autowired
    private BuyerMerchantStoreSupport buyerMerchantStoreSupport;

    @Operation(
            summary = "获取当前商户订单列表",
            description = "返回当前登录商户店铺的订单分页列表。支持按订单号、状态、商品名等筛选。"
    )
    @Parameters({
            @Parameter(name = "pageNumber", description = "页码，从1开始", example = "1"),
            @Parameter(name = "pageSize", description = "每页数量", example = "10"),
            @Parameter(name = "orderSn", description = "订单号（模糊搜索）", example = "202604010001"),
            @Parameter(name = "goodsName", description = "商品名称（模糊搜索）", example = "iPhone"),
            @Parameter(name = "orderStatus", description = "订单状态，如 UNPAID/UNDELIVERED/DELIVERED/COMPLETED/CANCELLED", example = "UNDELIVERED")
    })
    @GetMapping("/mine/list")
    public ResultMessage<IPage<OrderSimpleVO>> queryMineOrder(OrderSearchParams orderSearchParams) {
        return ResultUtil.data(orderService.queryByStoreId(orderSearchParams, getCurrentStoreId()));
    }

    @Operation(
            summary = "查看订单详情",
            description = "根据订单号查询订单详情。仅允许查看当前商户店铺的订单。"
    )
    @Parameter(name = "orderSn", description = "订单号", required = true, example = "202604010001")
    @GetMapping("/{orderSn}")
    public ResultMessage<OrderDetailVO> detail(@NotBlank @PathVariable String orderSn) {
        Order order = checkStoreOrder(orderSn, getCurrentStoreId());
        if (order == null) {
            throw new ServiceException(ResultCode.ORDER_NOT_EXIST);
        }
        return ResultUtil.data(orderService.queryDetail(orderSn));
    }

    @Operation(
            summary = "修改订单金额",
            description = "修改当前商户店铺订单的应付金额。仅允许对未支付订单改价，金额必须大于0。"
    )
    @Parameters({
            @Parameter(name = "orderSn", description = "订单号", required = true, example = "202604010001"),
            @Parameter(name = "orderPrice", description = "修改后的订单总金额（>0）", required = true, example = "99.99")
    })
    @PutMapping("/{orderSn}/price")
    public ResultMessage<Object> updateOrderPrice(@PathVariable String orderSn,
                                                  @NotNull(message = "订单价格不能为空") @RequestParam Double orderPrice) {
        checkStoreOrder(orderSn, getCurrentStoreId());
        if (NumberUtil.isGreater(Convert.toBigDecimal(orderPrice), Convert.toBigDecimal(0))) {
            return ResultUtil.data(orderPriceService.updatePrice(orderSn, orderPrice));
        }
        return ResultUtil.error(ResultCode.ORDER_PRICE_ERROR);
    }

    private Order checkStoreOrder(String orderSn, String currentStoreId) {
        Order order = orderService.getBySn(orderSn);
        if (order == null) {
            throw new ServiceException(ResultCode.ORDER_NOT_EXIST);
        }
        if (!currentStoreId.equals(order.getStoreId())) {
            throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
        }
        return order;
    }

    private String getCurrentStoreId() {
        return buyerMerchantStoreSupport.requireCurrentStoreId();
    }
}
