package cn.lili.modules.message.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 通知类站内信模版对象
 *
 * @author Bulbasaur
 * @version v4.1
 * @since 2020/12/8 9:46
 */
@Data
@TableName("li_notice_message")
@Schema(description = "通知类消息模板")
@EqualsAndHashCode(callSuper = false)
public class NoticeMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "站内信节点")
    @NotEmpty(message = "站内信节点不能为空")
    @Length(max = 50, message = "站内信节点名称太长,不能超过50")
    private String noticeNode;

    @Schema(description = "站内信标题")
    @NotEmpty(message = "站内信标题不能为空")
    @Length(max = 50, message = "站内信标题名称太长,不能超过50")
    private String noticeTitle;

    @Schema(description = "站内信内容")
    @NotEmpty(message = "站内信内容不能为空")
    @Length(max = 200, message = "站内信内容名称太长，不能超过200")
    private String noticeContent;
    /**
     * @see cn.lili.common.enums.SwitchEnum
     */
    @NotEmpty(message = "站内信状态不能为空")
    @Schema(description = "站内信是否开启")
    private String noticeStatus;
    /**
     * @see cn.lili.modules.message.entity.enums.NoticeMessageParameterEnum
     */
    @Schema(description = "消息变量")
    @NotEmpty(message = "站内信状态不能为空")
    private String variable;


}