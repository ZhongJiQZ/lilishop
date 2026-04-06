package cn.lili.controller.eid;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.TencentEidUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import com.tencentcloudapi.faceid.v20180301.models.DetectInfoText;
import com.tencentcloudapi.faceid.v20180301.models.GetEidResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 买家端 E 证通：建议顺序
 * <ol>
 *   <li>{@code GET /buyer/eid/getToken} — 姓名+身份证号换核身链接</li>
 *   <li>{@code GET /buyer/eid/getResult} — 用户完成核身后，凭 eidToken 拉结果并落库</li>
 *   <li>{@code PUT /buyer/store/store/apply/first} — 提交店铺申请第一步（企业资料），与核身记录校验</li>
 * </ol>
 */
@RestController
@Tag(name = "买家端,E证通接口")
@RequestMapping("/buyer/eid")
public class EidController {

    private final TencentEidUtil tencentEidUtil;
    private final MemberService memberService;

    public EidController(TencentEidUtil tencentEidUtil, MemberService memberService) {
        this.tencentEidUtil = tencentEidUtil;
        this.memberService = memberService;
    }

    @Operation(summary = "获取 E 证通核身链接",
            description = "参数：姓名 name、身份证号 idCard。返回 url、eidToken，前端拉起核身；未完成第三步申请前勿混用其它资料接口。")
    @GetMapping("/getToken")
    public ResultMessage<TencentEidUtil.EidTokenResult> getToken(
            @Parameter(description = "姓名，与身份证一致", required = true) @RequestParam String name,
            @Parameter(description = "18 位身份证号", required = true) @RequestParam String idCard) {
        if (CharSequenceUtil.hasBlank(name, idCard)) {
            throw new ServiceException(ResultCode.PARAMS_ERROR);
        }
        TencentEidUtil.EidTokenResult result = tencentEidUtil.getEidToken(name.trim(), idCard.trim());
        return ResultUtil.data(result);
    }

    @Operation(summary = "查询 E 证通核身结果",
            description = "需登录。写入 li_member_eid_record；成功后回写会员姓名、证件号。再调 apply/first 提交入驻资料。")
    @GetMapping("/getResult")
    public ResultMessage<GetEidResultResponse> getResult(
            @Parameter(description = "getToken 返回的 eidToken", required = true) @RequestParam String eidToken) {
        if (CharSequenceUtil.isBlank(eidToken)) {
            throw new ServiceException(ResultCode.PARAMS_ERROR);
        }
        AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
        GetEidResultResponse resp = tencentEidUtil.getEidResultAndSave(eidToken.trim(), authUser.getId());
        DetectInfoText text = resp.getText();
        if (text != null && Objects.equals(text.getErrCode(), 0L)) {
            Member member = memberService.getById(authUser.getId());
            if (member != null && CharSequenceUtil.isNotEmpty(text.getName())) {
                member.setFullName(text.getName());
                if (CharSequenceUtil.isNotEmpty(text.getIdCard())) {
                    member.setIdCard(text.getIdCard());
                }
                memberService.updateById(member);
            }
        }
        return ResultUtil.data(resp);
    }
}
