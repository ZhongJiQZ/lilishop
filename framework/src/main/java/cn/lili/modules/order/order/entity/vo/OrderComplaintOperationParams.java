package cn.lili.modules.order.order.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 交易投诉 参数
 *
 * @author paulG
 * @since 2020/12/4
 **/
@Data
public class OrderComplaintOperationParams {


    @Schema(description = "要更改的状态状态")
    private String complainStatus;

    @Schema(description = "交易投诉主键")
    private String complainId;

    @Schema(description = "商家申诉内容")
    private String appealContent;

    @Schema(description = "商家申诉上传的图片")
    private List<String> images;

    @Schema(description = "仲裁结果")
    private String arbitrationResult;

}
