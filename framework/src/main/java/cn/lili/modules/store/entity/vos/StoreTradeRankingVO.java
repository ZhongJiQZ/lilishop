package cn.lili.modules.store.entity.vos;

import cn.lili.modules.goods.entity.vos.GoodsTradeRankingVO;
import cn.lili.modules.store.entity.dos.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺成交排行榜VO
 *
 * @author lensing
 * @since 2026-03-25 17:02:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StoreTradeRankingVO extends Store {

    @Schema(description = "排名序号")
    private Integer rank;

    @Schema(description = "店铺ID")
    private String id;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "店铺Logo")
    private String storeLogo;

    @Schema(description = "成交订单数")
    private Long orderCount;

    @Schema(description = "商品列表")
    private List<GoodsTradeRankingVO> goodsList = new ArrayList<>();

}
