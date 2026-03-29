package cn.lili.modules.im.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 聊天礼物操作 DTO
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImChatGiftOperationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "礼物名称", required = true)
    @NotEmpty(message = "礼物名称不能为空")
    private String giftName;

    @Schema(description = "礼物图标", required = true)
    @NotEmpty(message = "礼物图标不能为空")
    private String giftImage;

    @Min(message = "必须为数字", value = 0)
    @Schema(description = "价格(平台币)", required = true)
    private BigDecimal coinPrice;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态 0禁用 1启用")
    private Integer status;

}