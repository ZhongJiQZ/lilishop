package cn.lili.controller.im;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.im.entity.dos.ImTalk;
import cn.lili.modules.im.entity.dto.IMTalkQueryParams;
import cn.lili.modules.im.entity.vo.ImTalkVO;
import cn.lili.modules.im.service.ImTalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Chopper
 */
@RestController
@Tag(name = "聊天接口")
@RequestMapping("/im/talk")
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImTalkController {

    private final ImTalkService imTalkService;

    @Operation(summary = "查看聊天详情")
    @Parameter(name = "id", description = "聊天ID", required = true)
    @GetMapping("/{id}")
    public ResultMessage<ImTalk> get(@PathVariable String id) {

        ImTalk imTalk = imTalkService.getById(id);
        return ResultUtil.data(imTalk);
    }

    @Operation(summary = "查看与某人聊天详情")
    @Parameter(name = "uid", description = "用户ID", required = true)
    @GetMapping("/user/{uid}")
    public ResultMessage<ImTalk> getUser(@PathVariable String uid) {
        //通过长度判断,保证每次都是同一个聊天
        return ResultUtil.data(imTalkService.getTalkByUser(uid));
    }

    @Operation(summary = "查看与某人聊天详情")
    @Parameter(name = "userId", description = "用户ID", required = true)
    @GetMapping("/by/user/{userId}")
    public ResultMessage<ImTalkVO> getByUser(@PathVariable String userId) {
        return ResultUtil.data(imTalkService.getTalkByUserId(userId));
    }

    @Operation(summary = "设置聊天置顶")
    @Parameter(name = "id", description = "聊天ID", required = true)
    @Parameter(name = "top", description = "是否置顶", required = true)
    @GetMapping("/top")
    public ResultMessage<Object> top(String id, Boolean top) {
        imTalkService.top(id, top);
        return ResultUtil.success();
    }

    @Operation(summary = "分页获取用户聊天")
    @Parameter(name = "imTalkQueryParams", description = "聊天查询参数", required = true)
    @GetMapping("/list")
    public ResultMessage<List<ImTalkVO>> getUserTalkList(IMTalkQueryParams imTalkQueryParams) {
        return ResultUtil.data(imTalkService.getUserTalkList(imTalkQueryParams));
    }

    @Operation(summary = "分页获取商家聊天")
    @Parameter(name = "imTalkQueryParams", description = "聊天查询参数", required = true)
    @GetMapping("/store/list")
    public ResultMessage<List<ImTalkVO>> getStoreTalkList(IMTalkQueryParams imTalkQueryParams) {
        return ResultUtil.data(imTalkService.getStoreTalkList(imTalkQueryParams));
    }

    @Operation(summary = "删除聊天")
    @Parameter(name = "id", description = "聊天ID", required = true)
    @DeleteMapping("/{id}")
    public ResultMessage<Object> disable(@PathVariable String id) {
        imTalkService.disable(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }
}
