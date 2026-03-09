package cn.lili.modules.order.order.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 发票
 *
 * @author Bulbasaur
 * @since 2020/11/28 11:38
 */
@Data
@Schema(description = "发票")
public class ReceiptVO implements Serializable {

    private static final long serialVersionUID = -8402457457074092957L;

    @Schema(description = "发票抬头")
    private String receiptTitle;

    @Schema(description = "纳税人识别号")
    private String taxpayerId;

    @Schema(description = "发票内容")
    private String receiptContent;

}
