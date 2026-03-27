package cn.lili.modules.im.entity.dos;


import cn.lili.mybatis.BaseTenantEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author lensing
 */
@Data
@TableName("li_im_chat_reward")
@Schema(description = "打赏记录表")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImChatReward extends BaseTenantEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "礼物ID")
    private String giftId;

    @Schema(description = "礼物名称")
    private String giftName;

    @Schema(description = "礼物图片")
    private String giftImage;

    @Schema(description = "礼物单价（平台币）")
    private BigDecimal coinPrice;

    @Schema(description = "礼物数量")
    private Integer num;

    @Schema(description = "本次打赏总消耗平台币")
    private BigDecimal totalCoin;

    @Schema(description = "打赏人ID（用户）")
    private String fromMemberId;

    @Schema(description = "打赏人名称")
    private String fromMemberName;

    @Schema(description = "打赏人头像")
    private String fromMemberAvatar;

    @Schema(description = "被打赏人ID（试穿员）")
    private String toMemberId;

    @Schema(description = "被打赏人名称")
    private String toMemberName;

    @Schema(description = "被打赏人头像")
    private String toMemberAvatar;
}