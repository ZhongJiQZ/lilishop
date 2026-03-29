package cn.lili.controller.im;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.goods.entity.dto.GoodsOperationDTO;
import cn.lili.modules.im.entity.dos.ImChatGift;
import cn.lili.modules.im.entity.dto.ImChatGiftOperationDTO;
import cn.lili.modules.im.entity.dto.ImChatGiftPageDTO;
import cn.lili.modules.im.service.ImChatGiftService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端,聊天礼物管理接口
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
@RestController
@Tag(name = "管理端,聊天礼物管理接口")
@RequestMapping("/manager/im/chat/gift")
public class ChatGiftManagerController {
    /**
     * 聊天礼物
     */
    @Autowired
    private ImChatGiftService chatGiftService;

    @Operation(summary = "分页获取")
    @Parameter(name = "page", description = "聊天礼物查询参数")
    @GetMapping("/list")
    public ResultMessage<IPage<ImChatGift>> getByPage(ImChatGiftPageDTO page) {
        return ResultUtil.data(chatGiftService.queryImChatGiftByParams(page));
    }

    @Operation(summary = "获取聊天礼物")
    @Parameter(name = "id", description = "聊天礼物ID", required = true)
    @GetMapping("/get/{id}")
    public ResultMessage<ImChatGift> getById(@NotNull @PathVariable String id) {
        ImChatGift chatGift = chatGiftService.getById(id);
        return ResultUtil.data(chatGift);
    }

    @Operation(summary = "发布聊天礼物")
    @PostMapping(value = "/create")
    public ResultMessage<GoodsOperationDTO> save(@Valid @RequestBody ImChatGiftOperationDTO chatGiftOperationDTO) {
        ImChatGift chatGift = new ImChatGift();
        BeanUtils.copyProperties(chatGiftOperationDTO,chatGift);
        chatGiftService.save(chatGift);
        return ResultUtil.success();
    }

    @Operation(summary = "删除聊天礼物")
    @Parameter(name = "ids", description = "聊天礼物ID", required = true)
    @DeleteMapping("/delete/{ids}")
    public ResultMessage<Object> delete(@PathVariable List<String> ids) {
        chatGiftService.removeBatchByIds(ids);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @Operation(summary = "编辑聊天礼物")
    @Parameter(name = "id", description = "聊天礼物ID", required = true)
    @PutMapping("/{id}")
    public ResultMessage<ImChatGift> update(@NotNull @PathVariable String id, @Valid @RequestBody ImChatGiftOperationDTO chatGiftOperationDTO) {
        ImChatGift chatGift = chatGiftService.getById(id);
        if(chatGift == null){
            return ResultUtil.error(ResultCode.CHAT_GIFT_NOT_EXIST);
        }
        BeanUtils.copyProperties(chatGiftOperationDTO,chatGift);
        chatGiftService.updateById(chatGift);
        return ResultUtil.data(chatGift);
    }

}
