package cn.lili.modules.store.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 店铺结算日
 *
 * @author Chopper
 * @since 2021/2/20 3:24 下午
 */
@Data
public class StoreSettlementDay {

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "结算日")
    private Date settlementDay;
}
