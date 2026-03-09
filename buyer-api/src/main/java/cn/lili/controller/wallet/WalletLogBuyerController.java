package cn.lili.controller.wallet;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.wallet.entity.dos.WalletLog;
import cn.lili.modules.wallet.service.WalletLogService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 买家端,预存款变动日志记录接口
 *
 * @author pikachu
 * @since 2020/11/16 10:07 下午
 */
@RestController
@Tag(name = "买家端,预存款变动日志记录接口")
@RequestMapping("/buyer/wallet/log")
public class WalletLogBuyerController {

    @Autowired
    private WalletLogService walletLogService;

    @Operation(description = "分页获取预存款变动日志")
    @Parameter(name = "page", description = "分页参数")
    @GetMapping
    public ResultMessage<IPage<WalletLog>> getByPage(PageVO page) {
        //获取当前登录用户
        AuthUser authUser = UserContext.getCurrentUser();
        //构建查询 返回数据
        IPage<WalletLog> depositLogPage = walletLogService.page(PageUtil.initPage(page),
                new QueryWrapper<WalletLog>().eq("member_id", authUser.getId()).orderByDesc("create_time"));
        return ResultUtil.data(depositLogPage);
    }
}
