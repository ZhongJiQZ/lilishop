package cn.lili.modules.goods.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 管理端：从模板店铺批量复制商品到多个目标店铺
 */
@Data
@Schema(description = "批量复制模板商品参数")
public class BatchCopyTemplateGoodsDTO {

    @NotEmpty(message = "模板商品不能为空")
    @Schema(description = "模板商品ID列表（SPU id）")
    private List<String> templateGoodsIds;

    @NotEmpty(message = "目标店铺不能为空")
    @Schema(description = "目标店铺ID列表")
    private List<String> targetStoreIds;
}
