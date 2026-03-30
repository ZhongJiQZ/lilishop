package cn.lili.modules.im.entity.dto;

import cn.lili.common.utils.StringUtils;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.im.entity.dos.ImChatReward;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RewardQueryParams
 *
 * @author lensig
 * @version v1.0
 * 2026-03-25 17:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RewardQueryParams extends PageVO {

    private static final long serialVersionUID = 3504156704697214077L;

    /**
     * 礼物ID
     */
    @Schema(description = "礼物ID")
    private String giftId;
    /**
     * 试穿员ID（店铺ID）
     */
    @Schema(description = "试穿员ID（店铺ID）")
    private String toMemberId;
    /**
     * 打赏人ID（用户）
     */
    @Schema(description = "打赏人ID（用户）")
    private String fromMemberId;
    /**
     * 数量
     */
    @Schema(description = "数量")
    private Integer num = 1;

    public LambdaQueryWrapper<ImChatReward> initQueryWrapper() {
        LambdaQueryWrapper<ImChatReward> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(toMemberId)){
            lambdaQueryWrapper.eq(ImChatReward::getToMemberId,toMemberId);
        }
        if(StringUtils.isNotBlank(fromMemberId)){
            lambdaQueryWrapper.eq(ImChatReward::getFromMemberId,fromMemberId);
        }
        lambdaQueryWrapper.orderByDesc(ImChatReward::getCreateTime);
//        lambdaQueryWrapper.last("limit " + num);
        return lambdaQueryWrapper;
    }
}
