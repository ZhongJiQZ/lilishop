package cn.lili.modules.store.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商家使用电子面单客户号DTO
 *
 * @author chc
 * @since 2022-4-13 10:00:58
 */
@Data
@Schema
public class StoreLogisticsCustomerDTO {

    @Schema(description = "客户代码")
    private String customerName;

    @Schema(description = "客户密码")
    private String customerPwd;

    @Schema(description = "密钥")
    private String monthCode;

    @Schema(description = "归属网点/网点编码")
    private String sendSite;

    @Schema(description = "收件快递员")
    private String sendStaff;

    @Schema(description = "是否使用电子面单")
    private boolean faceSheetFlag;

    @Schema(description = "支付方式")
    private String payType;

    @Schema(description = "快递类型")
    private String expType;

    @Schema(description = "电子面单客户账户名称")
    private String partnerName;

}
