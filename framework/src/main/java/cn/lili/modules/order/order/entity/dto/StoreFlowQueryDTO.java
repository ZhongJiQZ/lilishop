package cn.lili.modules.order.order.entity.dto;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.store.entity.dos.Bill;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 店铺流水查询DTO
 *
 * @author Chopper
 * @version v1.0
 * 2021-12-08 10:53
 */
@Data
@Builder
public class StoreFlowQueryDTO {

    @Schema(description = "类型")
    private String type;

    @Schema(description = "售后编号")
    private String refundSn;

    @Schema(description = "订单编号")
    private String orderSn;

    @Schema(description = "订单货物编号")
    private String orderItemSn;

    @Schema(description = "过滤只看分销订单")
    private Boolean justDistribution;

    @Schema(description = "结算单")
    private Bill bill;

    @Schema(description = "分页")
    private PageVO pageVO;

}
