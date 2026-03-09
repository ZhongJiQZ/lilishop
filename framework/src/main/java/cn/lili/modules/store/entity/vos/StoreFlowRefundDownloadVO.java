package cn.lili.modules.store.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺流水下载
 *
 * @author Bulbasaur
 * @date: 2021/8/13 4:14 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StoreFlowRefundDownloadVO extends StoreFlowPayDownloadVO {

    @Schema(description = "售后SN")
    private String refundSn;

}
