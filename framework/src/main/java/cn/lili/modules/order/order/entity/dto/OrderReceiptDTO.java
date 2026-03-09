package cn.lili.modules.order.order.entity.dto;

import cn.lili.modules.order.order.entity.dos.Receipt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 订单发票
 *
 * @author lili
 * @since 2020/11/28 11:38
 */
@Data
@Schema(description = "订单发票")
@EqualsAndHashCode(callSuper = false)
public class OrderReceiptDTO extends Receipt {

    @Schema(description = "订单状态")
    private String orderStatus;

}
