package cn.lili.modules.wechat.entity.dos;

import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 微信消息
 *
 * @author Chopper
 * @version v4.0
 * @since 2020/12/10 17:02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_wechat_message")
@Schema(description = "微信消息")
public class WechatMessage extends BaseEntity {


    private static final long serialVersionUID = -9157586585885836755L;

    @Schema(description = "模版名称")
    private String name;

    @Schema(description = "微信模版码")
    private String code;

    @Schema(description = "关键字")
    private String keywords;

    @Schema(description = "是否开启")
    private Boolean enable = true;

    /**
     * @see OrderStatusEnum
     */
    @Schema(description = "订单状态")
    private String orderStatus;

    @Schema(description = "模版头部信息")
    private String first;

    @Schema(description = "模版备注（位于最下方）")
    private String remark;


}