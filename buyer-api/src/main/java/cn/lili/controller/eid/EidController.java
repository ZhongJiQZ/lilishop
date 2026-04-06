package cn.lili.controller.eid;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.TencentEidUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.eid.entity.vo.EidVerifyResultVO;
import cn.lili.modules.eid.service.MemberEidRecordService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.dos.StoreDetail;
import cn.lili.modules.store.entity.dto.TryOnStaffApplyDTO;
import cn.lili.modules.store.service.StoreDetailService;
import cn.lili.modules.store.service.StoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencentcloudapi.faceid.v20180301.models.DetectInfoText;
import com.tencentcloudapi.faceid.v20180301.models.GetEidResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 买家端 E 证通：资料提交 → 获取认证链接 → 查询结果并同步会员实名
 *
 * @author lensing
 * @since 2026/04/03 10:05 下午
 */
@RestController
@Tag(name = "买家端,E证通接口")
@RequestMapping("/buyer/eid")
public class EidController {

    private final TencentEidUtil tencentEidUtil;
    private final StoreService storeService;
    private final StoreDetailService storeDetailService;
    private final MemberEidRecordService memberEidRecordService;
    private final MemberService memberService;

    public EidController(TencentEidUtil tencentEidUtil,
                        StoreService storeService,
                        StoreDetailService storeDetailService,
                        MemberEidRecordService memberEidRecordService,
                        MemberService memberService) {
        this.tencentEidUtil = tencentEidUtil;
        this.storeService = storeService;
        this.storeDetailService = storeDetailService;
        this.memberEidRecordService = memberEidRecordService;
        this.memberService = memberService;
    }

    @Operation(summary = "提交/修改试穿员资料（E证通前置）",
            description = "未完成实名前可多次保存；已完成 E 证通核身成功则不可再改。不涉及调腾讯接口。")
    @PostMapping("/company")
    public ResultMessage<Object> saveCompany(@Valid @RequestBody TryOnStaffApplyDTO body) {
        storeService.saveTryOnStaffBeforeEidVerify(body);
        return ResultUtil.success();
    }

    @Operation(summary = "获取 E 证通认证链接",
            description = "根据已保存的法人姓名、身份证号换取 url 与 eidToken；已实名成功则不可再获取。")
    @GetMapping("/auth-url")
    public ResultMessage<TencentEidUtil.EidTokenResult> getAuthUrl() {
        AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
        if (memberEidRecordService.hasSuccessfulVerification(authUser.getId())) {
            throw new ServiceException(ResultCode.EID_ALREADY_VERIFIED);
        }
        String legalName;
        String legalId;
        Store store = storeService.getOne(new LambdaQueryWrapper<Store>().eq(Store::getMemberId, authUser.getId()), false);
        if (store == null) {
            throw new ServiceException(ResultCode.EID_COMPANY_REQUIRED);
        }
        StoreDetail detail = storeDetailService.getStoreDetail(store.getId());
        if (detail == null) {
            throw new ServiceException(ResultCode.EID_COMPANY_REQUIRED);
        }
        legalName = detail.getLegalName();
        legalId = detail.getLegalId();
        if (CharSequenceUtil.isBlank(legalName) || CharSequenceUtil.isBlank(legalId)) {
            throw new ServiceException(ResultCode.EID_COMPANY_REQUIRED);
        }
        TencentEidUtil.EidTokenResult result = tencentEidUtil.getEidToken(legalName.trim(), legalId.trim());
        return ResultUtil.data(result);
    }

    @Operation(summary = "查询 E 证通认证结果",
            description = "写入核身记录；若本次核身成功则同步会员姓名、证件号并返回成功标识。")
    @GetMapping("/result")
    public ResultMessage<EidVerifyResultVO> queryResult(
            @Parameter(description = "获取认证链接接口返回的 eidToken", required = true)
            @RequestParam String eidToken) {
        if (CharSequenceUtil.isBlank(eidToken)) {
            throw new ServiceException(ResultCode.PARAMS_ERROR);
        }
        AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
        GetEidResultResponse resp = tencentEidUtil.getEidResultAndSave(eidToken.trim(), authUser.getId());
        DetectInfoText text = resp.getText();
        boolean verified = text != null && Objects.equals(text.getErrCode(), 0L);
        if (verified) {
            Member member = memberService.getById(authUser.getId());
            if (member != null && CharSequenceUtil.isNotEmpty(text.getName())) {
                member.setFullName(text.getName());
                if (CharSequenceUtil.isNotEmpty(text.getIdCard())) {
                    member.setIdCard(text.getIdCard());
                }
                memberService.updateById(member);
            }
            return ResultUtil.data(new EidVerifyResultVO(true, "认证成功"));
        }
        String msg = text != null && CharSequenceUtil.isNotEmpty(text.getErrMsg())
                ? text.getErrMsg()
                : "认证未完成或失败";
        return ResultUtil.data(new EidVerifyResultVO(false, msg));
    }
}
