package cn.lili.modules.im.service;

import cn.lili.modules.im.entity.dos.ImChatReward;
import cn.lili.modules.im.entity.dos.ImMemberIncome;
import cn.lili.modules.im.entity.dto.ImChatRewardDTO;
import cn.lili.modules.im.entity.dto.ImChatRewardPageDTO;
import cn.lili.modules.im.entity.dto.RewardQueryParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 聊天打赏 业务层
 *
 * @author lensing
 */
public interface ImChatRewardService extends IService<ImChatReward> {


    void sendReward(@Valid ImChatRewardDTO dto);

    List<ImChatReward> getMyReward(RewardQueryParams rewardQueryParams);

    List<ImChatReward> getToMemberReward(RewardQueryParams rewardQueryParams);

    ImMemberIncome getMemberIncome(String memberId);

    IPage<ImChatReward> queryImChatRewardByParams(ImChatRewardPageDTO page);
}