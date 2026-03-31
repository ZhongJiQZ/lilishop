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
import cn.lili.modules.im.entity.dto.MessageQueryParams;
import cn.lili.modules.im.mapper.ImMessageMapper;
import cn.lili.modules.im.service.ImMessageService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.enums.CoinTypeEnum;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.store.service.StoreService;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.CoinSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
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
        readMessage(messageList);
        return messageList;
    }

    @Override
    public Long unreadMessageCount() {
        AuthUser currentUser = UserContext.getCurrentUser();
        if(currentUser == null){
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        return this.count(new LambdaQueryWrapper<ImMessage>().eq(ImMessage::getToUser,currentUser.getId()).eq(ImMessage::getIsRead,false));
    }

    @Override
    public void cleanUnreadMessage() {
        AuthUser currentUser = UserContext.getCurrentUser();
        if(currentUser == null){
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        this.update(new LambdaUpdateWrapper<ImMessage>().eq(ImMessage::getToUser,currentUser.getId()).set(ImMessage::getIsRead,true));
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
    public boolean deductPlatformCoin(String fromUserId, String toUserId) {
        try {
            // 1. 获取发送人信息
            Member fromMember = memberService.getById(fromUserId);
            if (fromMember == null) {
                log.error("发送用户不存在，用户ID：{}", fromUserId);
                return false;
            }

            // 2. 获取系统配置的消耗币数量
            CoinSetting coinSetting = getCoinSetting();
            if (coinSetting == null || coinSetting.getConsumer().compareTo(BigDecimal.ZERO) <= 0) {
                log.info("平台未开启消息消耗平台币功能");
                return true;
            }
            BigDecimal consumeCoin = coinSetting.getConsumer();

            // 3. 判断是否为商家/管理员 → 不扣费
            AuthUser authUser = UserContext.getCurrentUser();
            if (authUser != null) {
                if (UserEnums.STORE.equals(authUser.getRole())) {
                    log.info("商家发送消息，不消耗平台币");
                    return true;
                }
            }

            // 4. 判断发送人余额是否充足
            if (fromMember.getCoin() == null || fromMember.getCoin().compareTo(consumeCoin) < 0) {
                throw new ServiceException(ResultCode.USER_COINS_INSUFFICIENT_BALANCE);
            }

            // ============================
            // 5. 发送人扣币（会员）
            // ============================
            boolean deductSuccess = memberService.updateMemberCoin(
                    consumeCoin,
                    CoinTypeEnum.REDUCE.name(),
                    fromUserId,
                    "会员发送IM消息消耗平台币(" + consumeCoin + "币)"
            );
            if (!deductSuccess) {
                log.error("会员【{}】发送IM消息扣减平台币失败", fromUserId);
                return false;
            }

            // ============================
            // 6. 自动识别接收方：会员ID / 店铺ID
            // ============================
            String receiveMemberId = null;

            // 情况A：toId 是 会员ID
            Member toMember = memberService.getById(toUserId);
            if (toMember != null) {
                receiveMemberId = toMember.getId();
                log.info("接收方是会员：{}", receiveMemberId);
            }
            // 情况B：toId 是 店铺ID → 查询店铺对应的会员
            else {
                Member storeOwner = memberService.getOne(new LambdaQueryWrapper<Member>()
                        .eq(Member::getStoreId, toUserId)
                        .last("LIMIT 1"));
                if (storeOwner != null) {
                    receiveMemberId = storeOwner.getId();
                    log.info("接收方是店铺{}，对应会员：{}", toUserId, receiveMemberId);
                }
            }

            // ============================
            // 7. 给正确的接收方会员加币
            // ============================
            if (StrUtil.isNotBlank(receiveMemberId)) {
                memberService.updateMemberCoin(
                        consumeCoin,
                        CoinTypeEnum.INCREASE.name(),
                        receiveMemberId,
                        "接收IM消息获得平台币(" + consumeCoin + "币)"
                );
                log.info("接收方会员【{}】获得平台币：{}", receiveMemberId, consumeCoin);
            } else {
                log.warn("未找到有效接收会员，toId={}，不加币", toUserId);
            }

            log.info("会员【{}】发送消息成功，扣除平台币：{}", fromUserId, consumeCoin);
            return true;

        } catch (ServiceException e) {
            log.error("发送IM消息扣减平台币异常：{}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("发送IM消息扣减平台币系统异常", e);
            return false;
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
    private void readMessage(List<ImMessage> messageList) {
        if (!messageList.isEmpty()) {
            //判断用户类型
            AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
            String toUserId = "";
            if(UserEnums.MEMBER.equals(authUser.getRole())){
                toUserId = authUser.getId();
            }else if(UserEnums.STORE.equals(authUser.getRole())){
                toUserId = authUser.getStoreId();
            }
            //发送给自己的未读信息进行已读操作
            for (ImMessage imMessage : messageList) {
                if(Boolean.FALSE.equals(imMessage.getIsRead()) && imMessage.getToUser().equals(toUserId)){
                    imMessage.setIsRead(true);
                }
            }
        }
        this.updateBatchById(messageList);
    }

}