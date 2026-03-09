package cn.lili.controller.member;

import cn.lili.common.aop.annotation.PreventDuplicateSubmissions;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.enums.SwitchEnum;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.dos.MemberEvaluation;
import cn.lili.modules.member.entity.dto.EvaluationQueryParams;
import cn.lili.modules.member.entity.dto.MemberEvaluationDTO;
import cn.lili.modules.member.entity.vo.EvaluationNumberVO;
import cn.lili.modules.member.entity.vo.MemberEvaluationVO;
import cn.lili.modules.member.service.MemberEvaluationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * 买家端,会员商品评价接口
 *
 * @author Bulbasaur
 * @since 2020/11/16 10:08 下午
 */
@RestController
@Tag(name = "买家端,会员商品评价接口")
@RequestMapping("/buyer/member/evaluation")
public class MemberEvaluationBuyerController {

    /**
     * 会员商品评价
     */
    @Autowired
    private MemberEvaluationService memberEvaluationService;

    @PreventDuplicateSubmissions
    @Operation(summary = "添加会员评价")
    @PostMapping
    public ResultMessage<MemberEvaluationDTO> save(@Valid MemberEvaluationDTO memberEvaluationDTO) {
        return ResultUtil.data(memberEvaluationService.addMemberEvaluation(memberEvaluationDTO, true));
    }

    @Operation(summary = "查看会员评价详情")
    @Parameter(name = "id", description = "评价ID", required = true)
    @GetMapping("/get/{id}")
    public ResultMessage<MemberEvaluationVO> get(@NotNull(message = "评价ID不能为空") @PathVariable("id") String id) {
        return ResultUtil.data(memberEvaluationService.queryById(id));

    }

    @Operation(summary = "查看当前会员评价列表")
    @GetMapping
    public ResultMessage<IPage<MemberEvaluation>> queryMineEvaluation(EvaluationQueryParams evaluationQueryParams) {
        //设置当前登录会员
        evaluationQueryParams.setMemberId(UserContext.getCurrentUser().getId());
        return ResultUtil.data(memberEvaluationService.managerQuery(evaluationQueryParams));
    }

    @Operation(summary = "查看某一个商品的评价列表")
    @Parameter(name = "goodsId", description = "商品ID", required = true)
    @GetMapping("/{goodsId}/goodsEvaluation")
    public ResultMessage<IPage<MemberEvaluation>> queryGoodsEvaluation(EvaluationQueryParams evaluationQueryParams,
                                                                       @NotNull @PathVariable("goodsId") String goodsId) {
        //设置查询查询商品
        evaluationQueryParams.setGoodsId(goodsId);
        evaluationQueryParams.setStatus(SwitchEnum.OPEN.name());
        return ResultUtil.data(memberEvaluationService.managerQuery(evaluationQueryParams));
    }

    @Operation(summary = "查看某一个商品的评价数量")
    @Parameter(name = "goodsId", description = "商品ID", required = true)
    @GetMapping("/{goodsId}/evaluationNumber")
    public ResultMessage<EvaluationNumberVO> queryEvaluationNumber(@NotNull @PathVariable("goodsId") String goodsId) {
        return ResultUtil.data(memberEvaluationService.getEvaluationNumber(goodsId));
    }
}
