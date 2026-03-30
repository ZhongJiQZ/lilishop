package cn.lili.controller.im;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.im.entity.dos.ImChatReward;
import cn.lili.modules.im.entity.dos.ImMemberIncome;
import cn.lili.modules.im.entity.dto.ImChatRewardDTO;
import cn.lili.modules.im.entity.dto.RewardQueryParams;
import cn.lili.modules.im.service.ImChatRewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Chopper
 */
@RestController
@Tag(name = "聊天打赏接口")
@RequestMapping("/im/reward")
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImRewardController {

    @Autowired
    private ImChatRewardService chatRewardService;

    @Operation(summary = "打赏")
    @PostMapping("/send")
    public ResultMessage sendReward(@RequestBody @Valid ImChatRewardDTO dto) {
        chatRewardService.sendReward(dto);
        return ResultUtil.success();
    }

    @Operation(summary = "我打赏的记录")
    @GetMapping("/my/list")
    public ResultMessage<List<ImChatReward>> myReward(RewardQueryParams rewardQueryParams) {
        return ResultUtil.data(chatRewardService.getMyReward(rewardQueryParams));
    }

    @Operation(summary = "试穿员收到的打赏记录")
    @GetMapping("/to/list")
    public ResultMessage<List<ImChatReward>> toReward(RewardQueryParams rewardQueryParams) {
        return ResultUtil.data(chatRewardService.getToMemberReward(rewardQueryParams));
    }

    @Operation(summary = "试穿员收益统计")
    @GetMapping("/income")
    public ResultMessage<ImMemberIncome> income(String memberId) {
        return ResultUtil.data(chatRewardService.getMemberIncome(memberId));
    }

}
