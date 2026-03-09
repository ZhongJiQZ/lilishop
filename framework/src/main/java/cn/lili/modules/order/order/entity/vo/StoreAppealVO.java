package cn.lili.modules.order.order.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单交易投诉VO
 *
 * @author paulG
 * @since 2020/12/4
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreAppealVO {

    @Schema(description = "投诉id")
    private String orderComplaintId;

    @Schema(description = "申诉商家内容")
    private String appealContent;

    @Schema(description = "申诉商家上传的图片")
    private String appealImages;
}
