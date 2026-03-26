package cn.lili.modules.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;

/**
 * 充值价格设置
 *
 * @author lensing
 * @since 2026/3/25 11:10 下午
 */
@Data
public class RechargeAmountSetting implements Serializable {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "金额")
    private Decimal price;

    @Schema(description = "描述说明")
    private String desc;


}
