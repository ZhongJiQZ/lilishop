package cn.lili.modules.message.entity.dos;

import cn.lili.modules.message.entity.enums.MessageStatusEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 会员接受消息对象
 *
 * @author Chopper
 * @since 2020-02-25 14:10:16
 */
@Data
@TableName("li_member_message")
@Schema(description = "会员消息")
@EqualsAndHashCode(callSuper = false)
public class MemberMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会员id")
    private String memberId;

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "关联消息id")
    private String messageId;

    /**
     * @see MessageStatusEnum
     */
    @Schema(description = "状态")
    private String status = MessageStatusEnum.UN_READY.name();

}