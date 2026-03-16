package cn.lili.modules.circle.entity.dto;

import cn.lili.common.vo.PageVO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 圈子帖子dto
 *
 * @author lensing
 * @since 2026-03-16 15:18:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Schema(description = "圈子帖子dto")
public class CirclePostPageDTO extends PageVO {

    private static final long serialVersionUID = 8906820486037326039L;

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "帖子内容")
    private String content;

    @Schema(description = "状态 1=正常 0=删除/隐藏")
    private Integer status = 1;

    @Schema(description = "是否在首页显示 1=是 0=否")
    private Integer isHomeShow = 0;
}
