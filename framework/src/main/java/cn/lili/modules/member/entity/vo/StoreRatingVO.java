package cn.lili.modules.member.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 评分VO
 *
 * @author Chopper
 * @since 2021/3/15 5:55 下午
 */
@Data
public class StoreRatingVO {

    @Schema(description = "物流评分")
    private String deliveryScore;

    @Schema(description = "服务评分")
    private String serviceScore;

    @Schema(description = "描述评分")
    private String descriptionScore;

}
