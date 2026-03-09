package cn.lili.modules.order.cart.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 店铺备注
 *
 * @author Chopper
 * @since 2020-03-25 2:30 下午
 */
@Data
public class StoreRemarkDTO implements Serializable {

    private static final long serialVersionUID = -6793274046513576434L;
    @Schema(description = "店铺id")
    private String storeId;

    @Schema(description = "备注")
    private String remark;

}
