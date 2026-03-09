package cn.lili.controller.passport.connect;


import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.connect.service.ConnectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 买家端,app/小程序 联合登录
 *
 * @author Chopper
 * @since 2020-11-25 19:29
 */
@RestController
@Tag(name = "买家端,app/小程序 联合登录")
@RequestMapping("/buyer/passport/connect/bind")
public class ConnectBuyerBindController {

    @Autowired
    private ConnectService connectService;

    @Operation(summary = "unionID 绑定")
    @Parameters({
            @Parameter(name = "unionID", description = "unionID", required = true),
            @Parameter(name = "type", description = "type", required = true)
    })
    @PostMapping
    public void unionIDBind(@RequestParam String unionID, @RequestParam String type) {
        connectService.bind(unionID, type);
    }

    @Operation(summary = "unionID 解绑")
    @Parameter(name = "type", description = "type", required = true)
    @PostMapping("/unbind")
    public void unionIDBind(@RequestParam String type) {
        connectService.unbind(type);
    }


    @GetMapping("/list")
    @Operation(summary = "绑定列表")
    public ResultMessage<List<String>> bindList() {
        return ResultUtil.data(connectService.bindList());
    }


}
