package cn.lili.modules.im.serviceimpl;

import cn.hutool.core.util.StrUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.sensitive.SensitiveWordsFilter;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.im.entity.dos.ImMessage;
import cn.lili.modules.im.entity.dos.ImTalk;
import cn.lili.modules.im.entity.dto.MessageQueryParams;
import cn.lili.modules.im.mapper.ImMessageMapper;
import cn.lili.modules.im.mapper.ImTalkMapper;
import cn.lili.modules.im.service.ImMessageService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.permission.entity.dos.AdminUser;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.service.StoreService;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.CoinSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import cn.lili.modules.wallet.entity.dos.MemberWallet;
import cn.lili.modules.wallet.entity.dto.MemberWalletUpdateDTO;
import cn.lili.modules.wallet.entity.enums.DepositServiceTypeEnum;
import cn.lili.modules.wallet.service.MemberWalletService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Im消息 业务实现
 *
 * @author Chopper
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage> implements ImMessageService {
    @Autowired
    private MemberService memberService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private MemberWalletService memberWalletService;
    @Autowired
    private ImTalkMapper talkMapper;

    @Override
    public void read(String talkId, String accessToken) {
        LambdaUpdateWrapper<ImMessage> updateWrapper = new LambdaUpdateWrapper<>();
        String userId = UserContext.getAuthUser(accessToken).getId();
        updateWrapper.eq(ImMessage::getTalkId, talkId);
        updateWrapper.eq(ImMessage::getToUser, userId);
        updateWrapper.set(ImMessage::getIsRead, true);
        this.update(updateWrapper);
    }

    @Override
    public List<ImMessage> unReadMessages(String accessToken) {
        String userId = UserContext.getAuthUser(accessToken).getId();
        LambdaQueryWrapper<ImMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImMessage::getToUser, userId);
        queryWrapper.eq(ImMessage::getIsRead, false);
        return this.list(queryWrapper);
    }

    @Override
    public List<ImMessage> historyMessage(String accessToken, String to) {
        String userId = UserContext.getAuthUser(accessToken).getId();
        LambdaQueryWrapper<ImMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(i -> i.eq(ImMessage::getToUser, userId).and(j -> j.eq(ImMessage::getFromUser, to)));
        queryWrapper.or(i -> i.eq(ImMessage::getToUser, to).and(j -> j.eq(ImMessage::getFromUser, userId)));
        queryWrapper.orderByDesc(ImMessage::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean hasNewMessage(String accessToken) {
        String userId = UserContext.getAuthUser(accessToken).getId();
        LambdaQueryWrapper<ImMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImMessage::getIsRead, false);
        queryWrapper.eq(ImMessage::getToUser, userId);
        return this.list(queryWrapper).size() > 0;

    }

    @Override
    public List<ImMessage> getList(MessageQueryParams messageQueryParams) {
        List<ImMessage> messageList = this.page(PageUtil.initPage(messageQueryParams), messageQueryParams.initQueryWrapper()).getRecords();
        messageList.forEach(message -> {
            message.setText(SensitiveWordsFilter.filter(message.getText()));
        });
        ListSort(messageList);
//        readMessage(messageList, messageQueryParams);
        if (StrUtil.isNotBlank(messageQueryParams.getTalkId())) {
            this.readAllByTalkId(messageQueryParams.getTalkId());
        }
        return messageList;
    }

    @Override
    public Long unreadMessageCount() {
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        return this.count(new LambdaQueryWrapper<ImMessage>().eq(ImMessage::getToUser, currentUser.getId()).eq(ImMessage::getIsRead, false));
    }

    @Override
    public void cleanUnreadMessage() {
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        this.update(new LambdaUpdateWrapper<ImMessage>().eq(ImMessage::getToUser, currentUser.getId()).set(ImMessage::getIsRead, true));
    }

    /**
     * 获取平台币设置
     *
     * @return 平台币设置
     */
    private CoinSetting getCoinSetting() {
        Setting setting = settingService.get(SettingEnum.COIN_SETTING.name());
        return new Gson().fromJson(setting.getSettingValue(), CoinSetting.class);
    }

    @Override
    public boolean deductPlatformCoin(String fromUserId, String toUserId, AdminUser adminUser, Member fromMember) {
        try {
            if(adminUser != null) {
                log.info("管理员发送消息，不消耗预存款");
                return true;
            }
            // 1. 判断是否为商家/管理员 → 不扣费
            Store store = storeService.getById(fromUserId);
            if(store != null || (fromMember!=null && fromMember.getHaveStore() && fromMember.getStoreId()!=null)) {
                log.info("商家发送消息，不消耗预存款");
                return true;
            }

            // 2. 获取发送人信息
            if (fromMember == null) {
                log.error("发送用户不存在，用户ID：{}", fromUserId);
                throw new ServiceException(ResultCode.USER_NOT_EXIST, "发送用户不存在");
            }

            // 3. 获取系统配置的消耗金额
            CoinSetting coinSetting = getCoinSetting();
            if (coinSetting == null || coinSetting.getConsumer().compareTo(BigDecimal.ZERO) <= 0) {
                log.info("平台未开启消息消耗预存款功能");
                return true;
            }
            BigDecimal consumeMoney = coinSetting.getConsumer();
            Double money = consumeMoney.doubleValue();

            // ============================
            // 4. 校验会员钱包余额（预存款）
            // ============================
            MemberWallet fromWallet = memberWalletService.getOne(new LambdaQueryWrapper<MemberWallet>().eq(MemberWallet::getMemberId, fromUserId));
            if (fromWallet == null) {
                fromWallet = memberWalletService.save(fromUserId, fromMember.getUsername());
            }
            if (fromWallet.getMemberWallet() < money) {
                throw new ServiceException(ResultCode.USER_COINS_INSUFFICIENT_BALANCE, "预存款余额不足，无法发送消息");
            }

            // ============================
            // 5. 发送人扣减预存款
            // ============================
            boolean deductSuccess = memberWalletService.reduce(new MemberWalletUpdateDTO(
                    money,
                    fromUserId,
                    "发送消息:" + money,
                    DepositServiceTypeEnum.WALLET_SEND_IM.name()
            ));
            if (!deductSuccess) {
                log.error("会员【{}】发送IM消息扣减预存款失败", fromUserId);
                throw new ServiceException(ResultCode.PLATFORM_COIN_OPERATION_FAILED, "消息发送失败，预存款扣减异常");
            }

            log.info("会员【{}】发送消息成功，扣除预存款：{} 元", fromUserId, money);
            return true;

        } catch (ServiceException e) {
            String errorMsg = StrUtil.isBlank(e.getMsg()) ? "消息发送失败" : e.getMsg();
            log.error("发送IM消息扣减预存款异常：{}", e.getMsg());
            throw new ServiceException(ResultCode.PLATFORM_COIN_OPERATION_FAILED, errorMsg);
        } catch (Exception e) {
            log.error("发送IM消息扣减预存款系统异常", e);
            throw new ServiceException(ResultCode.PLATFORM_COIN_OPERATION_FAILED, "消息发送异常，请稍后重试");
        }
    }

    @Override
    public Long unreadMessageTotalCount() {
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }

        // 区分用户类型：买家=userId，商家=storeId
        String storeId = null;
        if (UserEnums.STORE.equals(currentUser.getRole())) {
            storeId = currentUser.getStoreId();
        } else {
            Member member = memberService.getById(currentUser.getId());
            if (member != null && member.getHaveStore() && member.getStoreId() != null) {
                storeId = member.getStoreId();
            }
        }
        if (StringUtils.isBlank(storeId)) {
            throw new ServiceException(ResultCode.STORE_NOT_LOGIN_ERROR);
        }

        // 查询所有未读消息（toUser = 当前用户，isRead = false）
        return this.count(new LambdaQueryWrapper<ImMessage>()
                .eq(ImMessage::getToUser, storeId)
                .eq(ImMessage::getIsRead, false));
    }

    /**
     * 获取当前用户的未读IM消息总数（包括试穿员和会员）
     * @param to
     * @return 未读消息数
     */
    @Override
    public Long getUnreadCount(String to) {
        String toUser = to;

        // 先查是不是商家（storeId）
        Store store = storeService.getById(toUser);
        if (store != null) {
            // 是商家 → 用 storeId 统计
            toUser = toUser; // 本身就是 storeId
        } else {
            // 是会员 → 判断是不是试穿员（有店铺）
            Member member = memberService.getById(toUser);
            if (member != null && member.getHaveStore() && StrUtil.isNotBlank(member.getStoreId())) {
                // 试穿员 → 用 storeId 统计消息
                toUser = member.getStoreId();
            }
        }

        // 统计未读消息
        return this.count(new LambdaQueryWrapper<ImMessage>()
                .eq(ImMessage::getToUser, toUser)
                .eq(ImMessage::getIsRead, false));
    }

    /**
     * 根据时间倒叙
     *
     * @param list
     */
    private static void ListSort(List<ImMessage> list) {
        list.sort(new Comparator<ImMessage>() {
            @Override
            public int compare(ImMessage e1, ImMessage e2) {
                try {
                    if (e1.getCreateTime().before(e2.getCreateTime())) {
                        return -1;
                    } else {
                        return 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }


    /**
     * 阅读消息
     *
     * @param messageList 消息列表
     */
    private void readMessage(List<ImMessage> messageList, MessageQueryParams messageQueryParams) {
        if (!messageList.isEmpty()) {
            //判断用户类型
            AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
            Member member = memberService.getById(authUser.getId());
            String toUserId = "";
            Boolean self = false;//自身消息
            if (UserEnums.MEMBER.equals(authUser.getRole())) {
                ImTalk talk = talkMapper.selectById(messageQueryParams.getTalkId());
                if (member != null) {
                    if ((talk.getUserId1().equals(member.getId()) && talk.getUserId2().equals(member.getStoreId())) || talk.getUserId1().equals(member.getStoreId()) && talk.getUserId2().equals(member.getId())) {
                        self = true;
                    }
                }
                if (member != null && member.getHaveStore() && member.getStoreId() != null) {
                    toUserId = member.getStoreId();
                } else {
                    toUserId = authUser.getId();
                }
            } else if (UserEnums.STORE.equals(authUser.getRole())) {
                toUserId = authUser.getStoreId();
            }
            //发送给自己的未读信息进行已读操作
            for (ImMessage imMessage : messageList) {
                if (self) {
                    if (Boolean.FALSE.equals(imMessage.getIsRead()) && (imMessage.getToUser().equals(member.getId()) || imMessage.getToUser().equals(member.getStoreId()))) {
                        imMessage.setIsRead(true);
                    }
                } else {
                    if (Boolean.FALSE.equals(imMessage.getIsRead()) && imMessage.getToUser().equals(toUserId)) {
                        imMessage.setIsRead(true);
                    }
                }
            }
        }
        this.updateBatchById(messageList);
    }



    /**
     * 将某个对话的所有未读消息标为已读
     */
    public void readAllByTalkId(String talkId) {
        AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
        if (authUser == null) {
            return;
        }

        // 当前用户是谁（会员/试穿员用 id，店铺用 storeId）
        //判断用户类型
        Member member = memberService.getById(authUser.getId());
        String toUserId = "";
        if (UserEnums.MEMBER.equals(authUser.getRole())) {
            if (member != null && member.getHaveStore() && member.getStoreId() != null) {
                toUserId = member.getStoreId();
            } else {
                toUserId = authUser.getId();
            }
        } else if (UserEnums.STORE.equals(authUser.getRole())) {
            toUserId = authUser.getStoreId();
        } else if (UserEnums.MANAGER.equals(authUser.getRole())) {
            ImTalk talk = talkMapper.selectById(talkId);
            if (talk != null) {
                if(talk.getStoreFlag1()){
                    toUserId = talk.getUserId1();
                }else if(talk.getStoreFlag2()){
                    toUserId = talk.getUserId2();
                }
            }
        }

        // 条件：
        // 1. 属于当前 talkId
        // 2. 消息是发给我的
        // 3. 未读
        LambdaUpdateWrapper<ImMessage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ImMessage::getTalkId, talkId);
        wrapper.eq(ImMessage::getToUser, toUserId);
        wrapper.eq(ImMessage::getIsRead, false);
        wrapper.set(ImMessage::getIsRead, true);

        this.update(wrapper);
    }
}