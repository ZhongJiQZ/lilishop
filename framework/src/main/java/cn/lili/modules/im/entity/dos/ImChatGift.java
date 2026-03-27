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
@TableName("li_im_chat_gift")
@Schema(description = "聊天礼物表")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImChatGift extends BaseTenantEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "礼物名称")
    private String giftName;

    @Schema(description = "礼物图标")
    private String giftImage;

    @Schema(description = "价格(平台币)")
    private BigDecimal coinPrice;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态 0禁用 1启用")
    private Boolean status;
}