package cn.lili.modules.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 充值弹窗设置
 *
 * @author lensing
 * @since 2026/3/25 11:10 下午
 */
@Data
public class RechargePopupSetting implements Serializable {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "活动内容")
    private String content;

    @Schema(description = "跳转链接")
    private String url;


}
