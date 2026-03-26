package cn.lili.controller.member;
 
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.dos.MemberCoinsHistory;
import cn.lili.modules.member.entity.vo.MemberCoinsHistoryVO;
import cn.lili.modules.member.entity.vo.MemberCoinsStatisticsVO;
import cn.lili.modules.member.service.MemberCoinsHistoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,会员平台币历史接口
 *
 * @author lensing
 * @since 2026-03-25 14:10:16
 */
@RestController
@Tag(name = "管理端,会员平台币历史接口")
@RequestMapping("/manager/member/memberCoinsHistory")
public class MemberCoinsHistoryManagerController {
    @Autowired
    private MemberCoinsHistoryService memberCoinsHistoryService;

    @Operation(description = "分页获取")
    @Parameter(name = "page", description = "分页参数")
    @Parameter(name = "memberId", description = "会员ID")
    @Parameter(name = "memberName", description = "会员名称")
    @GetMapping("/getByPage")
    public ResultMessage<IPage<MemberCoinsHistory>> getByPage(PageVO page, String memberId, String memberName) {
        return ResultUtil.data(memberCoinsHistoryService.MemberCoinsHistoryList(page, memberId, memberName));
    }

    @Operation(description = "获取会员平台币VO")
    @Parameter(name = "memberId", description = "会员ID", required = true)
    @GetMapping("/getMemberCoinsHistoryVO")
    public ResultMessage<MemberCoinsHistoryVO> getMemberCoinsHistoryVO(String memberId) {
        return ResultUtil.data(memberCoinsHistoryService.getMemberCoinsHistoryVO(memberId));
    }

    @Operation(description = "获取平台币统计")
    @GetMapping("/queryMemberCoinsStatistics")
    public ResultMessage<MemberCoinsStatisticsVO> queryMemberCoinsStatistics() {
        return ResultUtil.data(memberCoinsHistoryService.queryMemberCoinsStatistics());
    }




}
