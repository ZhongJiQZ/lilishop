package cn.lili.modules.message.entity.dos;

import cn.lili.modules.message.entity.enums.MessageSendClient;
import cn.lili.modules.message.entity.enums.RangeEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 管理段发送消息对象
 *
 * @author lili
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_message")
@Schema(description = "消息")
public class Message extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    /**
     * @see RangeEnum
     */
    @Schema(description = "发送范围")
    private String messageRange;

    /**
     * @see MessageSendClient
     */
    @Schema(description = "发送客户端 商家或者会员")
    private String messageClient;

    @TableField(exist = false)
    @Schema(description = "发送指定用户id")
    private String[] userIds;

    @TableField(exist = false)
    @Schema(description = "发送指定用户名称")
    private String[] userNames;
}