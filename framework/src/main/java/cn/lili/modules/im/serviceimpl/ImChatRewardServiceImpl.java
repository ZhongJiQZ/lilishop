package cn.lili.modules.im.serviceimpl;

import cn.hutool.core.date.DateUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.im.entity.dos.ImChatGift;
import cn.lili.modules.im.entity.dos.ImChatReward;
import cn.lili.modules.im.entity.dos.ImMemberIncome;
import cn.lili.modules.im.entity.dto.ImChatRewardDTO;
import cn.lili.modules.im.entity.dto.ImChatRewardPageDTO;
import cn.lili.modules.im.entity.dto.RewardQueryParams;
import cn.lili.modules.im.mapper.ImChatGiftMapper;
import cn.lili.modules.im.mapper.ImChatRewardMapper;
import cn.lili.modules.im.mapper.ImMemberIncomeMapper;
import cn.lili.modules.im.service.ImChatRewardService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.enums.StoreStatusEnum;
import cn.lili.modules.store.service.StoreService;
import cn.lili.modules.wallet.entity.dos.MemberWallet;
import cn.lili.modules.wallet.entity.dto.MemberWalletUpdateDTO;
import cn.lili.modules.wallet.entity.enums.DepositServiceTypeEnum;
import cn.lili.modules.wallet.service.MemberWalletService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 聊天打赏 业务实现
 *
 * @author lensing
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImChatRewardServiceImpl extends ServiceImpl<ImChatRewardMapper, ImChatReward> implements ImChatRewardService {
    @Autowired
    private ImChatGiftMapper chatGiftMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ImMemberIncomeMapper memberIncomeMapper;
    @Autowired
    private StoreService storeService;
    @Autowired
    private MemberWalletService memberWalletService;

    @Override
    public void sendReward(ImChatRewardDTO dto) {
        //校验店铺
        checkStore(dto.getToMemberId());
        String userId = UserContext.getCurrentUser().getId();
        // 1. 获取礼物
        ImChatGift gift = chatGiftMapper.selectById(dto.getGiftId());
        if (gift == null) throw new ServiceException(ResultCode.PARAMS_ERROR);

        BigDecimal totalCoin = gift.getCoinPrice().multiply(new BigDecimal(dto.getNum()));
        Double money = totalCoin.doubleValue(); // 转为 Double 适配钱包接口

        // ====================== 2. 获取会员钱包 ======================
        Member member = memberService.getById(userId);
        if (member == null) throw new ServiceException(ResultCode.USER_NOT_EXIST);
        MemberWallet memberWallet = memberWalletService.save(userId, member.getUsername());

        // 判断余额是否充足
        if (memberWallet.getMemberWallet() < money) {
            throw new ServiceException(ResultCode.USER_COINS_INSUFFICIENT_BALANCE);
        }

        // ====================== 3. 扣减发送人余额 ======================
        boolean deductSuccess = this.memberWalletService.reduce(new MemberWalletUpdateDTO(
                money,
                userId,
                "会员打赏礼物-" + gift.getGiftName() + ":" + gift.getCoinPrice().doubleValue(),
                DepositServiceTypeEnum.WALLET_REWARD_GIFT.name()
        ));
        if (!deductSuccess) {
            throw new ServiceException(ResultCode.PLATFORM_COIN_OPERATION_FAILED, "打赏失败，余额扣减失败");
        }

        // ====================== 4. 获取被打赏人 ======================
        // 被打赏人信息（店铺所属会员）
        Member toMember = memberService.getOne(new LambdaQueryWrapper<Member>()
                .eq(Member::getStoreId, dto.getToMemberId())
                .last("LIMIT 1"));
        if (toMember == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST, "被打赏用户不存在");
        }

        // ====================== 5. 给被打赏人增加余额（Lilishop 标准方法） ======================
        boolean addSuccess = this.memberWalletService.increase(new MemberWalletUpdateDTO(
                money,
                toMember.getId(),
                "收到会员打赏礼物-" + gift.getGiftName() + ":" + gift.getCoinPrice().doubleValue(),
                DepositServiceTypeEnum.WALLET_REWARD_GIFT_RECEIVE.name()
        ));
        if (!addSuccess) {
            log.error("打赏功能——给被打赏人【{}】增加余额失败，金额：{}", toMember.getId(), money);
            throw new ServiceException(ResultCode.PLATFORM_COIN_OPERATION_FAILED, "打赏失败，余额发放异常");
        }

        // ====================== 6. 写入打赏记录 ======================
        ImChatReward reward = new ImChatReward();
        reward.setGiftId(gift.getId());
        reward.setGiftName(gift.getGiftName());
        reward.setGiftImage(gift.getGiftImage());
        reward.setCoinPrice(gift.getCoinPrice());
        reward.setNum(dto.getNum());
        reward.setTotalCoin(totalCoin);
        reward.setFromMemberId(userId);
        reward.setFromMemberName(member.getUsername());
        reward.setFromMemberAvatar(member.getFace());
        reward.setToMemberId(toMember.getId());
        reward.setToMemberName(toMember.getUsername());
        reward.setToMemberAvatar(toMember.getFace());
        this.save(reward);

        // ====================== 7. 更新试穿员收益 ======================
        ImMemberIncome income = memberIncomeMapper.selectOne(new LambdaQueryWrapper<ImMemberIncome>()
                .eq(ImMemberIncome::getMemberId, toMember.getId()));

        if (income == null) {
            income = new ImMemberIncome();
            income.setMemberId(toMember.getId());
            income.setMemberName(toMember.getUsername());
            income.setTotalIncome(totalCoin);
            income.setTodayIncome(totalCoin); // 第一次，直接赋值
            memberIncomeMapper.insert(income);
        } else {
            // 判断最后更新时间是不是【今天】
            Date judgeTime = income.getUpdateTime() != null ? income.getUpdateTime() : income.getCreateTime();
            boolean isToday = DateUtil.isSameDay(judgeTime, new Date());

            memberIncomeMapper.update(null, new LambdaUpdateWrapper<ImMemberIncome>()
                    .eq(ImMemberIncome::getId, income.getId())
                    .setSql("total_income = total_income + " + totalCoin) // 总收益永远累加
                    // 今天：累加；不是今天：重置为当前金额
                    .setSql("today_income = " + (isToday ? "today_income + " : "") + totalCoin)
            );
        }

        log.info("会员【{}】打赏礼物成功，消费余额：{} 元", userId, money);
    }

    @Override
    public List<ImChatReward> getMyReward(RewardQueryParams rewardQueryParams) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        rewardQueryParams.setFromMemberId(currentUser.getId());
        return this.page(PageUtil.initPage(rewardQueryParams), rewardQueryParams.initQueryWrapper()).getRecords();
    }

    @Override
    public List<ImChatReward> getToMemberReward(RewardQueryParams rewardQueryParams) {
        checkIsStore(rewardQueryParams.getToMemberId());
        return this.page(PageUtil.initPage(rewardQueryParams), rewardQueryParams.initQueryWrapper()).getRecords();
    }

    @Override
    public ImMemberIncome getMemberIncome(String memberId) {
        checkIsStore(memberId);
        ImMemberIncome income = memberIncomeMapper.selectOne(new LambdaQueryWrapper<ImMemberIncome>()
                .eq(ImMemberIncome::getMemberId, memberId));
        return income == null ? new ImMemberIncome() : income;
    }

    @Override
    public IPage<ImChatReward> queryImChatRewardByParams(ImChatRewardPageDTO page) {
        LambdaQueryWrapper<ImChatReward> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(page.getGiftName())) {
            queryWrapper.like(ImChatReward::getGiftName, page.getGiftName());
        }
        if (StringUtils.isNotEmpty(page.getFromMemberId())) {
            queryWrapper.like(ImChatReward::getFromMemberId, page.getFromMemberId());
        }
        if (StringUtils.isNotEmpty(page.getFromMemberName())) {
            queryWrapper.like(ImChatReward::getFromMemberName, page.getFromMemberName());
        }
        if (StringUtils.isNotEmpty(page.getToMemberId())) {
            queryWrapper.like(ImChatReward::getToMemberId, page.getToMemberId());
        }
        if (StringUtils.isNotEmpty(page.getToMemberName())) {
            queryWrapper.like(ImChatReward::getToMemberName, page.getToMemberName());
        }
        queryWrapper.orderByDesc(ImChatReward::getCreateTime);
        return this.page(PageUtil.initPage(page), queryWrapper);
    }

    /**
     * 校验是否为店铺
     * @param memberId
     */
    private void checkIsStore(String memberId) {
        Member toMember = memberService.getById(memberId);
        if (toMember == null) {
            throw new ServiceException(ResultCode.STORE_MEMBER_NOT_EXIST);
        }
        if(!toMember.getHaveStore() || StringUtils.isEmpty(toMember.getStoreId())){
            throw new ServiceException(ResultCode.STORE_NOT_OPEN);
        }
    }

    /**
     * 校验店铺
     * @param storeId
     */
    private Store checkStore(String storeId) {
        Store store = storeService.getById(storeId);
        if(store == null){
            throw new ServiceException(ResultCode.STORE_NOT_EXIST);
        }
        if(!store.getStoreDisable().equals(StoreStatusEnum.OPEN.name())){
            throw new ServiceException(ResultCode.STORE_CLOSE_ERROR);
        }
        return store;
    }
}