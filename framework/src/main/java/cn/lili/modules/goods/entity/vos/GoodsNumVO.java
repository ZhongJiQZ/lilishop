package cn.lili.modules.goods.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GoodsNumVO {

    @Schema(description = "出售中的商品数量")
    private Integer upperGoodsNum;
    @Schema(description = "仓库中的商品数量")
    private Integer downGoodsNum;
    @Schema(description = "待审核商品数量")
    private Integer auditGoodsNum;
    @Schema(description = "审核未通过数量")
    private Integer refuseGoodsNum;
}
