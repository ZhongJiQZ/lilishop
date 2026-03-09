package cn.lili.controller.member;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.dos.MemberGrade;
import cn.lili.modules.member.service.MemberGradeService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端,会员等级接口
 *
 * @author Bulbasaur
 * @since 2021/5/16 11:29 下午
 */
@RestController
@Tag(name = "管理端,会员等级接口")
@RequestMapping("/manager/member/memberGrade")
public class MemberGradeManagerController {

    @Autowired
    private MemberGradeService memberGradeService;

    @Operation(description = "通过id获取会员等级")
    @Parameter(name = "id", description = "会员等级ID", required = true)
    @GetMapping("/get/{id}")
    public ResultMessage<MemberGrade> get(@PathVariable String id) {

        return ResultUtil.data(memberGradeService.getById(id));
    }

    @Operation(description = "获取会员等级分页")
    @Parameter(name = "page", description = "分页参数")
    @GetMapping("/getByPage")
    public ResultMessage<IPage<MemberGrade>> getByPage(PageVO page) {

        return ResultUtil.data(memberGradeService.page(PageUtil.initPage(page)));
    }

    @Operation(description = "添加会员等级")
    @Parameter(name = "memberGrade", description = "会员等级")
    @PostMapping
    public ResultMessage<Object> add(@Validated MemberGrade memberGrade) {
        if (memberGradeService.save(memberGrade)) {
            return ResultUtil.success(ResultCode.SUCCESS);
        }
        return ResultUtil.error(ResultCode.ERROR);
    }

    @Operation(description = "修改会员等级")
    @Parameter(name = "id", description = "会员等级ID", required = true)
    @Parameter(name = "memberGrade", description = "会员等级")
    @PutMapping("/update/{id}")
    public ResultMessage<Object> update(@PathVariable String id, MemberGrade memberGrade) {
        if (memberGradeService.updateById(memberGrade)) {
            return ResultUtil.success(ResultCode.SUCCESS);
        }
        return ResultUtil.error(ResultCode.ERROR);
    }

    @Operation(description = "删除会员等级")
    @Parameter(name = "id", description = "会员等级ID", required = true)
    @DeleteMapping("/delete/{id}")
    public ResultMessage<IPage<Object>> delete(@PathVariable String id) {
        if (memberGradeService.removeById(id)) {
            return ResultUtil.success(ResultCode.SUCCESS);
        }
        return ResultUtil.error(ResultCode.ERROR);
    }
}
