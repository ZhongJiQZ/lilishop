package cn.lili.modules.message.entity.vos;

import cn.lili.modules.message.entity.enums.MessageStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 会员接收消息查询vo
 *
 * @author Chopper
 * @since 2020/12/2 17:50
 */
@Data
@Schema(description = "会员接收消息查询vo")
public class MemberMessageQueryVO {

    private static final long serialVersionUID = 1L;

    /**
     * @see MessageStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    @Schema(description = "消息id")
    private String messageId;

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "会员id")
    private String memberId;

}