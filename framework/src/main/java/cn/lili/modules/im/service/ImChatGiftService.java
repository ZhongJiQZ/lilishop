package cn.lili.modules.im.service;

import cn.lili.modules.im.entity.dos.ImChatGift;
import cn.lili.modules.im.entity.dto.ImChatGiftPageDTO;
import cn.lili.modules.im.entity.dto.ImChatGiftQueryParams;
import cn.lili.modules.im.entity.vo.ImChatGiftVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 聊天礼物 业务层
 *
 * @author lensing
 */
public interface ImChatGiftService extends IService<ImChatGift> {

    List<ImChatGiftVO> getGiftList(ImChatGiftQueryParams chatGiftQueryParams);

    IPage<ImChatGift> queryImChatGiftByParams(ImChatGiftPageDTO page);
}