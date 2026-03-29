package cn.lili.controller.im;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.im.entity.dto.ImChatGiftQueryParams;
import cn.lili.modules.im.entity.vo.ImChatGiftVO;
import cn.lili.modules.im.service.ImChatGiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author lensing
 */
@RestController
@Tag(name = "聊天礼物接口")
@RequestMapping("/im/chat/gift")
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImChatGiftController {

    @Autowired
    private ImChatGiftService chatGiftService;

    @Operation(summary = "礼物面板")
    @GetMapping("/list")
    public ResultMessage<List<ImChatGiftVO>> listGift(ImChatGiftQueryParams chatGiftQueryParams) {
        return ResultUtil.data(chatGiftService.getGiftList(chatGiftQueryParams));
    }

}
