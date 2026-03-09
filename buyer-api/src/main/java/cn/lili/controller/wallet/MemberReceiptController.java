package cn.lili.controller.wallet;


import cn.lili.common.aop.annotation.PreventDuplicateSubmissions;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.vo.MemberReceiptAddVO;
import cn.lili.modules.member.entity.vo.MemberReceiptVO;
import cn.lili.modules.member.service.MemberReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 买家端,会员发票接口
 *
 * @author paulG
 * @since 2021-03-29 14:10:16
 */
@RestController
@Tag(name = "买家端,会员发票接口")
@RequestMapping("/buyer/wallet/receipt")
public class MemberReceiptController {

    @Autowired
    private MemberReceiptService memberReceiptService;

    @Operation(summary = "查询会员发票列表")
    @GetMapping
    public ResultMessage<Object> page(MemberReceiptVO memberReceiptVO, PageVO page) {
        return ResultUtil.data(memberReceiptService.getPage(memberReceiptVO, page));
    }

    @PreventDuplicateSubmissions
    @Operation(summary = "新增会员发票")
    @PostMapping
    public ResultMessage<Object> add(MemberReceiptAddVO memberReceiptAddVO) {
        return ResultUtil.data(memberReceiptService.addMemberReceipt(memberReceiptAddVO, UserContext.getCurrentUser().getId()));
    }

    @Operation(summary = "修改会员发票")
    @Parameter(name = "id", description = "会员发票id", required = true)
    @PutMapping
    public ResultMessage<Object> update(@PathVariable String id, MemberReceiptAddVO memberReceiptAddVO) {
        memberReceiptAddVO.setId(id);
        return ResultUtil.data(memberReceiptService.editMemberReceipt(memberReceiptAddVO, id));
    }

    @Operation(summary = "会员发票删除")
    @Parameter(name = "id", description = "会员发票id", required = true)
    @DeleteMapping
    public ResultMessage<Boolean> deleteMessage(@PathVariable String id) {
        return ResultUtil.data(memberReceiptService.deleteMemberReceipt(id));
    }

}
