package cn.lili.controller.store;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dto.OrderSearchParams;
import cn.lili.modules.order.order.entity.vo.OrderDetailVO;
import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
import cn.lili.modules.order.order.service.OrderPriceService;
import cn.lili.modules.order.order.service.OrderService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.service.StoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 买家端-商户工作台订单接口
 */
@Tag(name = "买家端-商户工作台订单接口")
@RestController
@RequestMapping("/buyer/store/order")
public class BuyerStoreOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderPriceService orderPriceService;

    @Autowired
    private StoreService storeService;

    @Operation(summary = "获取当前商户订单列表")
    @GetMapping("/mine/list")
    public ResultMessage<IPage<OrderSimpleVO>> queryMineOrder(OrderSearchParams orderSearchParams) {
        return ResultUtil.data(orderService.queryByStoreId(orderSearchParams, getCurrentStoreId()));
    }

    @Operation(summary = "查看订单详情")
    @GetMapping("/{orderSn}")
    public ResultMessage<OrderDetailVO> detail(@NotBlank @PathVariable String orderSn) {
        Order order = checkStoreOrder(orderSn, getCurrentStoreId());
        if (order == null) {
            throw new ServiceException(ResultCode.ORDER_NOT_EXIST);
        }
        return ResultUtil.data(orderService.queryDetail(orderSn));
    }

    @Operation(summary = "修改订单金额")
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
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        if (CharSequenceUtil.isNotEmpty(currentUser.getStoreId())) {
            return currentUser.getStoreId();
        }
        Store store = storeService.getOne(new LambdaQueryWrapper<Store>().eq(Store::getMemberId, currentUser.getId()), false);
        if (store == null) {
            throw new ServiceException("当前账号未绑定店铺");
        }
        return store.getId();
    }
}
