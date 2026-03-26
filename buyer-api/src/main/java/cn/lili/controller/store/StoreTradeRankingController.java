package cn.lili.controller.store;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.store.entity.vos.StoreTradeRankingVO;
import cn.lili.modules.store.service.StoreService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,店铺成交排行榜接口
 *
 * @author lensing
 * @since 2026/03/26 2:32 下午
 */
@RestController
@RequestMapping("/buyer/store/trade")
@Tag(name = "买家端,店铺接口")
public class StoreTradeRankingController {
    @Autowired
    private StoreService storeService;

    @Operation(summary = "获取店铺成交排行榜（按成交单数降序）")
    @GetMapping("/ranking")
    public ResultMessage<IPage<StoreTradeRankingVO>> getTradeRanking(PageVO page) {
        return ResultUtil.data(storeService.getTradeRanking(page));
    }
}
