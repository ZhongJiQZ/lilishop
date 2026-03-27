package cn.lili.modules.im.entity.dto;

import cn.lili.common.vo.PageVO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 聊天礼物dto
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Schema(description = "聊天礼物dto")
public class ImChatGiftPageDTO extends PageVO {

    private static final long serialVersionUID = 8906820486037326039L;

    @Schema(description = "礼物名称")
    private String giftName;

    @Schema(description = "礼物图标")
    private String giftImage;

    @Schema(description = "状态 1=启用 0=禁用")
    private Integer status;

}
