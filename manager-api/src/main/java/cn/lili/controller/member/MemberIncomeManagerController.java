package cn.lili.controller.member;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.im.entity.dos.ImMemberIncome;
import cn.lili.modules.im.service.ImMemberIncomeService;
import cn.lili.modules.member.entity.dto.ImMemberIncomePageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,会员打赏收益管理接口
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
@RestController
@Tag(name = "管理端,会员打赏收益管理接口")
@RequestMapping("/manager/member/income")
public class MemberIncomeManagerController {
    /**
     * 会员打赏收益
     */
    @Autowired
    private ImMemberIncomeService memberIncomeService;

    @Operation(summary = "分页获取")
    @Parameter(name = "page", description = "会员收益查询参数")
    @GetMapping("/list")
    public ResultMessage<IPage<ImMemberIncome>> getByPage(ImMemberIncomePageDTO page) {
        return ResultUtil.data(memberIncomeService.queryImMemberIncomeByParams(page));
    }

}
