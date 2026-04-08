package cn.lili.controller.eid;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.eid.entity.dos.MemberEidRecord;
import cn.lili.modules.eid.entity.dto.MemberEidRecordPageDTO;
import cn.lili.modules.eid.service.MemberEidRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端,会员E证通核身记录管理接口
 *
 * @author lensing
 * @since 2026-03-16 15:18:56
 */
@RestController
@Tag(name = "管理端,会员E证通核身记录管理接口")
@RequestMapping("/manager/eid")
public class MemberEidRecordManagerController {
    /**
     * 会员E证通核身记录
     */
    @Autowired
    private MemberEidRecordService memberEidRecordService;

    @Operation(summary = "分页获取")
    @Parameter(name = "memberEidRecordSearchParams", description = "会员E证通核身记录查询参数")
    @GetMapping("/list")
    public ResultMessage<IPage<MemberEidRecord>> getByPage(MemberEidRecordPageDTO page) {
        return ResultUtil.data(memberEidRecordService.queryMemberEidRecordByParams(page));
    }

    @Operation(summary = "获取会员E证通核身记录")
    @Parameter(name = "id", description = "会员E证通核身记录ID", required = true)
    @GetMapping("/get/{id}")
    public ResultMessage<MemberEidRecord> getById(@NotNull @PathVariable String id) {
        MemberEidRecord memberEidRecord = memberEidRecordService.getById(id);
//        memberEidRecord.setContent(SensitiveWordsFilter.filter(memberEidRecord.getContent()));
        return ResultUtil.data(memberEidRecord);
    }

    @Operation(summary = "删除会员E证通核身记录")
    @Parameter(name = "ids", description = "会员E证通核身记录ID", required = true)
    @DeleteMapping("/delete/{ids}")
    public ResultMessage<Object> delete(@PathVariable List<String> ids) {
        memberEidRecordService.removeByIds(ids);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
