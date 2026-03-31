package cn.lili.controller.member;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.dos.MemberCoinsHistory;
import cn.lili.modules.member.entity.vo.MemberCoinsHistoryVO;
import cn.lili.modules.member.service.MemberCoinsHistoryService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,会员平台币历史接口
 *
 * @author lensing
 * @since 2026-03-25 14:10:16
 */
@RestController
@Tag(name = "买家端,会员平台币历史接口")
@RequestMapping("/buyer/member/memberCoinsHistory")
public class CoinsHistoryBuyerController {
    @Autowired
    private MemberCoinsHistoryService memberCoinsHistoryService;

    @Operation(summary = "分页获取")
    @GetMapping("/getByPage")
    public ResultMessage<IPage<MemberCoinsHistory>> getByPage(PageVO page) {

        LambdaQueryWrapper<MemberCoinsHistory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MemberCoinsHistory::getMemberId, UserContext.getCurrentUser().getId());
        queryWrapper.orderByDesc(MemberCoinsHistory::getCreateTime);
        return ResultUtil.data(memberCoinsHistoryService.page(PageUtil.initPage(page), queryWrapper));
    }

    @Operation(summary = "获取会员平台币VO")
    @GetMapping("/getMemberCoinsHistoryVO")
    public ResultMessage<MemberCoinsHistoryVO> getMemberCoinsHistoryVO() {
        return ResultUtil.data(memberCoinsHistoryService.getMemberCoinsHistoryVO(UserContext.getCurrentUser().getId()));
    }


}
