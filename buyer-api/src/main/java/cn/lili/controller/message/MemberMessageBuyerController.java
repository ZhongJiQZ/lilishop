package cn.lili.controller.message;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.message.entity.dos.MemberMessage;
import cn.lili.modules.message.entity.enums.MessageStatusEnum;
import cn.lili.modules.message.entity.vos.MemberMessageQueryVO;
import cn.lili.modules.message.service.MemberMessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 买家端,会员站内消息接口
 *
 * @author Bulbasaur
 * @since 2020/11/16 10:07 下午
 */
@RestController
@Tag(name = "买家端,会员站内消息接口")
@RequestMapping("/buyer/message/member")
public class MemberMessageBuyerController {

    /**
     * 会员站内消息
     */
    @Autowired
    private MemberMessageService memberMessageService;

    @Operation(summary = "分页获取会员站内消息")
    @GetMapping
    public ResultMessage<IPage<MemberMessage>> page(MemberMessageQueryVO memberMessageQueryVO, PageVO page) {
        memberMessageQueryVO.setMemberId(UserContext.getCurrentUser().getId());
        return ResultUtil.data(memberMessageService.getPage(memberMessageQueryVO, page));
    }

    @Operation(summary = "消息已读")
    @Parameter(name = "messageId", description = "会员消息id", required = true)
    @PutMapping("/{message_id}")
    public ResultMessage<Boolean> read(@PathVariable("message_id") String messageId) {
        return ResultUtil.data(memberMessageService.editStatus(MessageStatusEnum.ALREADY_READY.name(), messageId));
    }

    @Operation(summary = "消息放入回收站")
    @Parameter(name = "messageId", description = "会员消息id", required = true)
    @DeleteMapping("/{message_id}")
    public ResultMessage<Boolean> deleteMessage(@PathVariable("message_id") String messageId) {
        return ResultUtil.data(memberMessageService.editStatus(MessageStatusEnum.ALREADY_REMOVE.name(), messageId));

    }


}
