package cn.lili.modules.im.entity.dto;

import cn.lili.common.utils.StringUtils;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.im.entity.dos.ImChatGift;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ChatGiftQueryParams
 *
 * @author lensig
 * @version v1.0
 * 2026-03-25 17:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImChatGiftQueryParams extends PageVO {

    private static final long serialVersionUID = 3504156704697214077L;

    /**
     * 礼物名称
     */
    private String giftName;
    /**
     * 价格
     */
    private String coinPrice;

    public LambdaQueryWrapper<ImChatGift> initQueryWrapper() {
        LambdaQueryWrapper<ImChatGift> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(giftName)){
            lambdaQueryWrapper.like(ImChatGift::getGiftName, giftName);
        }
        lambdaQueryWrapper.eq(ImChatGift::getStatus, 1);
        lambdaQueryWrapper.orderByAsc(ImChatGift::getSort);
        lambdaQueryWrapper.orderByAsc(ImChatGift::getId);
//        lambdaQueryWrapper.last("limit " + num);
        return lambdaQueryWrapper;
    }
}
