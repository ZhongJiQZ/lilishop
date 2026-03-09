package cn.lili.controller.store;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.store.entity.dos.StoreAddress;
import cn.lili.modules.store.service.StoreAddressService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 买家端,商家地址（自提点）接口
 *
 * @author chc
 * @since 2022/6/2114:46
 */
@RestController
@Tag(name = "买家端,商家地址（自提点）接口")
@RequestMapping("/buyer/store/address")
public class StoreAddressBuyerController {

    /**
     * 店铺自提点
     */
    @Autowired
    private StoreAddressService storeAddressService;

    @Operation(summary = "获取商家自提点分页")
    @Parameter(name = "storeId", description = "店铺Id", required = true)
    @GetMapping("/page/{storeId}")
    public ResultMessage<IPage<StoreAddress>> get(PageVO pageVo,@PathVariable String storeId) {
        return ResultUtil.data(storeAddressService.getStoreAddress(storeId, pageVo));
    }

    @Operation(summary = "获取商家自提点信息")
    @Parameter(name = "id", description = "自提点ID", required = true)
    @GetMapping("/{id}")
    public ResultMessage<StoreAddress> get(@PathVariable String id) {
        StoreAddress address = OperationalJudgment.judgment(storeAddressService.getById(id));
        return ResultUtil.data(address);
    }
}
