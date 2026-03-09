package cn.lili.modules.order.aftersale.entity.vo;

import cn.lili.modules.order.aftersale.entity.dos.AfterSale;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 售后VO
 *
 * @author Chopper
 * @since 2021/3/12 10:32 上午
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AfterSaleVO extends AfterSale {
    /**
     * 初始化自身状态
     */
    public AfterSaleAllowOperation getAfterSaleAllowOperationVO() {

        //设置订单的可操作状态
        return new AfterSaleAllowOperation(this);
    }
}
