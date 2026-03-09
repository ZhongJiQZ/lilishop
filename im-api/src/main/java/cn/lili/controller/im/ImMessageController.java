package cn.lili.controller.im;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.im.entity.dos.ImMessage;
import cn.lili.modules.im.entity.dto.MessageQueryParams;
import cn.lili.modules.im.service.ImMessageService;
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
@Tag(name = "Im消息接口")
@RequestMapping("/im/message")
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImMessageController {

    private final ImMessageService imMessageService;

    @Operation(summary = "查看Im消息详情")
    @Parameter(name = "id", description = "Im消息ID", required = true)
    @GetMapping("/{id}")
    public ResultMessage<ImMessage> get(@PathVariable String id) {
        ImMessage imMessage = imMessageService.getById(id);
        return ResultUtil.data(imMessage);
    }

    @Operation(summary = "分页获取Im消息")
    @Parameter(name = "messageQueryParams", description = "查询参数", required = true)
    @GetMapping
    public ResultMessage<List<ImMessage>> historyMessage(MessageQueryParams messageQueryParams) {
        List<ImMessage> data = imMessageService.getList(messageQueryParams);
        return ResultUtil.data(data);
    }

    @Operation(summary = "新增Im消息")
    @Parameter(name = "imMessage", description = "Im消息对象", required = true)
    @PostMapping
    public ResultMessage<ImMessage> save(ImMessage imMessage) {
        if (imMessageService.save(imMessage)) {
            return ResultUtil.data(imMessage);
        }
        throw new ServiceException(ResultCode.IM_MESSAGE_ADD_ERROR);
    }

    @Operation(summary = "更新Im消息")
    @Parameter(name = "id", description = "Im消息ID", required = true)
    @Parameter(name = "imMessage", description = "Im消息对象", required = true)
    @PutMapping("/{id}")
    public ResultMessage<ImMessage> update(@PathVariable String id, ImMessage imMessage) {
        if (imMessageService.updateById(imMessage)) {
            return ResultUtil.data(imMessage);
        }
        throw new ServiceException(ResultCode.IM_MESSAGE_EDIT_ERROR);
    }

    @Operation(summary = "删除Im消息")
    @Parameter(name = "ids", description = "Im消息ID列表", required = true)
    @DeleteMapping("/{ids}")
    public ResultMessage<Object> delAllByIds(@PathVariable List<String> ids) {
        imMessageService.removeByIds(ids);
        return ResultUtil.success();
    }


    @Operation(summary = "查看是否有新消息")
    @Parameter(name = "accessToken", description = "访问令牌", required = true)
    @GetMapping("/newMessage")
    public ResultMessage<Boolean> hasNewMessage(String accessToken) {
        return ResultUtil.data(imMessageService.hasNewMessage(accessToken));
    }

    @Operation(summary = "获取所有未读消息")
    @GetMapping("/unreadMessage")
    public ResultMessage<Long> getUnreadMessageCount() {
        return ResultUtil.data(imMessageService.unreadMessageCount());
    }

    @Operation(summary = "清除所有未读消息")
    @PutMapping("/clean/unred")
    public ResultMessage<Object> cleanUnreadMessage() {
        imMessageService.cleanUnreadMessage();
        return ResultUtil.success();
    }
}
