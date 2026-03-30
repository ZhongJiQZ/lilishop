package cn.lili.modules.goods.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品VO
 *
 * @author pikachu
 * @since 2020-02-26 23:24:13
 */
@Data
public class GoodsTradeRankingVO {

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "缩略图路径")
    private String thumbnail;

    @Schema(description = "商品视频")
    private String goodsVideo;

    @Schema(description = "商品类型")
    private String goodsType;

}
