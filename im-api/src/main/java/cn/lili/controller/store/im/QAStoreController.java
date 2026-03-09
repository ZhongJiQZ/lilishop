package cn.lili.controller.store.im;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.im.entity.dos.QA;
import cn.lili.modules.im.service.QAService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * 管理端,自定义分词接口
 *
 * @author paulG
 * @since 2020/10/16
 **/
@Slf4j
@RestController
@Tag(name = "管理端,自定义分词接口")
@RequestMapping("/im/store/qa")
public class QAStoreController {

    @Autowired
    private QAService qaService;

    @Operation(description = "添加问答")
    @Parameter(name = "qa", description = "问答", required = true)
    @PostMapping
    public ResultMessage<QA> addCustomWords(@Valid QA qa) {
        qaService.save(qa);
        return ResultUtil.data(qa);
    }

    @Operation(description = "修改自定义问答")
    @Parameter(name = "qa", description = "问答", required = true)
    @PutMapping
    public ResultMessage<QA> updateCustomWords(@Valid QA qa) {
        qaService.updateById(qa);
        return ResultUtil.data(qa);
    }

    @Operation(description = "删除自定义问答")
    @Parameter(name = "id", description = "问答ID", required = true)
    @DeleteMapping("/{id}")
    public ResultMessage<String> deleteCustomWords(@NotNull @PathVariable String id) {
        qaService.removeById(id);
        return ResultUtil.success();
    }

    @Operation(description = "分页获取自定义问答")
    @Parameter(name = "word", description = "问题", required = true)
    @Parameter(name = "pageVo", description = "分页参数", required = true)
    @GetMapping("/page")
    public ResultMessage<IPage<QA>> getCustomWords(@RequestParam String word, PageVO pageVo) {
        return ResultUtil.data(qaService.getStoreQA(word, pageVo));
    }

}
