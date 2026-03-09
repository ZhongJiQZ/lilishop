package cn.lili.modules.wechat.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信小程序消息订阅
 *
 * @author Chopper
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_wechat_mp_message")
@Schema(description = "微信小程序消息订阅")
public class WechatMPMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @Schema(description = "模版id")
    private String templateId;

    @Schema(description = "模版名称")
    private String name;

    @Schema(description = "微信模版码")
    private String code;

    @Schema(description = "关键字")
    private String keywords;

    @Schema(description = "关键字描述（小程序发送消息时使用）")
    private String keywordsText;

    @Schema(description = "是否开启")
    private Boolean enable = true;

    @Schema(description = "订单状态")
    private String orderStatus;
}