package cn.lili.modules.goods.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "批量复制模板商品结果")
public class BatchCopyTemplateGoodsResultVO {

    @Schema(description = "成功复制次数（店铺×商品）")
    private int successCount;

    @Schema(description = "跳过次数（已复制过）")
    private int skippedCount;

    @Schema(description = "失败次数")
    private int failedCount;

    @Schema(description = "明细（截断）")
    private List<String> details;
}
