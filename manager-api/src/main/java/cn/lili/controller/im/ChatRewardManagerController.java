package cn.lili.controller.im;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.im.entity.dos.ImChatReward;
import cn.lili.modules.im.entity.dto.ImChatRewardPageDTO;
import cn.lili.modules.im.service.ImChatRewardService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端,聊天打赏管理接口
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
@RestController
@Tag(name = "管理端,聊天打赏管理接口")
@RequestMapping("/manager/im/chat/reward")
public class ChatRewardManagerController {
    /**
     * 聊天打赏
     */
    @Autowired
    private ImChatRewardService chatGiftService;

    @Operation(summary = "分页获取")
    @Parameter(name = "page", description = "聊天打赏查询参数")
    @GetMapping("/list")
    public ResultMessage<IPage<ImChatReward>> getByPage(ImChatRewardPageDTO page) {
        return ResultUtil.data(chatGiftService.queryImChatRewardByParams(page));
    }

    @Operation(summary = "获取聊天打赏")
    @Parameter(name = "id", description = "聊天打赏ID", required = true)
    @GetMapping("/get/{id}")
    public ResultMessage<ImChatReward> getById(@NotNull @PathVariable String id) {
        ImChatReward chatGift = chatGiftService.getById(id);
        return ResultUtil.data(chatGift);
    }

    @Operation(summary = "删除聊天打赏")
    @Parameter(name = "ids", description = "聊天打赏ID", required = true)
    @DeleteMapping("/delete/{ids}")
    public ResultMessage<Object> delete(@PathVariable List<String> ids) {
        chatGiftService.removeBatchByIds(ids);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
