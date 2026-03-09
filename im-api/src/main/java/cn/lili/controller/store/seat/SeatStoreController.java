package cn.lili.controller.store.seat;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.im.entity.vo.SeatVO;
import cn.lili.modules.im.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SeatController
 *
 * @author Chopper
 * @version v1.0
 * 2022-02-10 11:50
 */
@RestController
@Tag(name = "店铺端,坐席管理")
@RequestMapping("/store/seat/setting")
@Transactional(rollbackFor = Exception.class)
public class SeatStoreController {


    @Autowired
    private SeatService seatService;

    @Operation(description = "分页获取坐席")
    @GetMapping("/list")
    public ResultMessage<List<SeatVO>> getSeats() {
        return ResultUtil.data(seatService.seatVoList(UserContext.getCurrentUser().getTenantId()));
    }


}
