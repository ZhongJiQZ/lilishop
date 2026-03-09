package cn.lili.controller.store.seat;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.im.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 坐席登录接口
 *
 * @author Chopper
 * @version v1.0
 * 2022-02-10 16:40
 */
@Slf4j
@RestController
@Tag(name = "坐席端")
@RequestMapping("/seat/login")
public class SeatLogin {

    @Autowired
    private SeatService seatService;

    @Operation(description = "登录接口")
    @Parameter(name = "username", description = "用户名", required = true)
    @Parameter(name = "password", description = "密码", required = true)
    @PostMapping("/userLogin")
    public ResultMessage<Object> userLogin(String username, String password) {
        return ResultUtil.data(this.seatService.usernameLogin(username, password));
    }

    @Operation(description = "商家快捷登录客服")
    @Parameter(name = "code", description = "验证码", required = true)
    @PostMapping("/quicklogin")
    public ResultMessage<Object> quickLogin(String code) {
        return ResultUtil.data(this.seatService.quickLogin(code));
    }


    @Operation(description = "登出")
    @PostMapping("/logout")
    public ResultMessage<Object> logout() {
        //todo
//        UserContext.getCurrentUser().getId()
//        verificationServiceClient.check(uuid);
        return ResultUtil.success();
    }


}
