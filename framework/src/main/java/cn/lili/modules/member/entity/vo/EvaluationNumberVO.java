package cn.lili.modules.member.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 评价数量VO
 *
 * @author Chopper
 * @since 2021/1/27 10:41 上午
 */
@Data
public class EvaluationNumberVO {

    @Schema(description = "全部评价")
    private Integer all;

    @Schema(description = "好评数量")
    private Integer good;

    @Schema(description = "中评数量")
    private Integer moderate;

    @Schema(description = "差评数量")
    private Integer worse;

    @Schema(description = "有图数量")
    private Long haveImage;
}
