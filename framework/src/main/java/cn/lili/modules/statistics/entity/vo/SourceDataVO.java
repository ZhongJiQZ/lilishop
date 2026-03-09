package cn.lili.modules.statistics.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 收款构成
 *
 * @author Bulbasaur
 * @since 2025/08/25 7:07 下午
 */
@Data
public class SourceDataVO {

    @Schema(description = "支付方式")
    private String payType;
    @Schema(description = "收款合计")
    private Double total;
    @Schema(description = "营业收入")
    private Double income;
    @Schema(description = "新增储值金额")
    private Double recharge;


}
