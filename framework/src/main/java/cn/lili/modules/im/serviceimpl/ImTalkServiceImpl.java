package cn.lili.modules.im.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.modules.im.entity.dos.ImMessage;
import cn.lili.modules.im.entity.dos.ImTalk;
import cn.lili.modules.im.entity.dto.IMTalkQueryParams;
import cn.lili.modules.im.entity.vo.ImTalkVO;
import cn.lili.modules.im.mapper.ImTalkMapper;
import cn.lili.modules.im.service.ImMessageService;
import cn.lili.modules.im.service.ImTalkService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.enums.StoreStatusEnum;
import cn.lili.modules.store.service.StoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 聊天 业务实现
 *
 * @author Chopper
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImTalkServiceImpl extends ServiceImpl<ImTalkMapper, ImTalk> implements ImTalkService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ImMessageService imMessageService;

    @Override
    public ImTalk getTalkByUser(String userId) {
        LambdaQueryWrapper<ImTalk> queryWrapper = new LambdaQueryWrapper<>();
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        //登录用户的Id
        String selfId = "";
        //查看当前用户角色对Id进行赋值
        if (UserEnums.STORE.equals(currentUser.getRole())) {
            selfId = currentUser.getStoreId();
        } else if (UserEnums.MEMBER.equals(currentUser.getRole())) {
            selfId = currentUser.getId();
        }
        //小数在前保证永远是同一个对话
        String finalSelfId = selfId;
        queryWrapper.and(wq -> wq.eq(ImTalk::getUserId2, userId).eq(ImTalk::getUserId1, finalSelfId).or().eq(ImTalk::getUserId2, finalSelfId).eq(ImTalk::getUserId1, userId));
        ImTalk imTalk = this.getOne(queryWrapper);
        //如果没有聊天，则创建聊天
        if (imTalk == null) {
            //当自己为店铺时
            if (UserEnums.STORE.equals(currentUser.getRole())) {
                Store selfStore = storeService.getById(selfId);
                //没有这个用户信息
                Member other = memberService.getById(userId);
                if (other == null) {
                    return null;
                }
                //自己为店铺其他人必定为用户
                imTalk = new ImTalk(other, selfStore);
            } else if (UserEnums.MEMBER.equals(currentUser.getRole())) {
                //没有这个店铺信息
                Member self = memberService.getById(selfId);
                Member otherMember = memberService.getById(userId);
                Store otherStore = storeService.getById(userId);
                if (otherStore != null) {
                    imTalk = new ImTalk(self, otherStore);
                } else if (otherMember != null) {
                    imTalk = new ImTalk(self, otherMember);
                }
            }
            this.save(imTalk);
        }
        return imTalk;
    }

    @Override
    public ImTalkVO getTalkByUserId(String userId) {
        LambdaQueryWrapper<ImTalk> queryWrapper = new LambdaQueryWrapper<>();
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        //登录用户的Id
        String selfId = "";
        //查看当前用户角色对Id进行赋值
        if (UserEnums.STORE.equals(currentUser.getRole())) {
            selfId = currentUser.getStoreId();
        } else if (UserEnums.MEMBER.equals(currentUser.getRole())) {
            selfId = currentUser.getId();
        }
        //小数在前保证永远是同一个对话
        String finalSelfId = selfId;
        queryWrapper.and(wq -> wq.eq(ImTalk::getUserId2, userId).eq(ImTalk::getUserId1, finalSelfId).or().eq(ImTalk::getUserId2, finalSelfId).eq(ImTalk::getUserId1, userId));
        ImTalk imTalk = this.getOne(queryWrapper);
        //如果没有聊天，则创建聊天
        if (imTalk == null) {
            //当自己为店铺时
            if (UserEnums.STORE.equals(currentUser.getRole())) {
                Store selfStore = storeService.getById(selfId);
                //没有这个用户信息
                Member other = memberService.getById(userId);
                if (other == null) {
                    return null;
                }
                //自己为店铺其他人必定为用户
                imTalk = new ImTalk(other, selfStore);
            } else if (UserEnums.MEMBER.equals(currentUser.getRole())) {
                //没有这个店铺信息
                Member self = memberService.getById(selfId);
                Member otherMember = memberService.getById(userId);
                Store otherStore = storeService.getById(userId);
                if (otherStore != null) {
                    imTalk = new ImTalk(self, otherStore);
                } else if (otherMember != null) {
                    imTalk = new ImTalk(self, otherMember);
                }
            }
            this.save(imTalk);
        }
        return new ImTalkVO(imTalk, currentUser.getId());
    }

    /**
     * 发起聊天后，如果聊天不可见为true，则需要修正
     *
     * @param imTalk 对话信息
     */
    private ImTalk check(ImTalk imTalk) {
        if (imTalk.getDisable1() || imTalk.getDisable2()) {
            imTalk.setDisable1(false);
            imTalk.setDisable2(false);
            this.updateById(imTalk);

        }
        return imTalk;
    }

    @Override
    public void top(String id, Boolean top) {
        ImTalk imTalk = this.getById(id);
        if (imTalk.getUserId1().equals(UserContext.getCurrentUser().getId())) {
            imTalk.setTop1(top);
        } else if (imTalk.getUserId2().equals(UserContext.getCurrentUser().getId())) {
            imTalk.setTop2(top);
        } else {
            throw new ServiceException(ResultCode.ERROR);
        }
        this.updateById(imTalk);
    }

    @Override
    public void disable(String id) {
        ImTalk imTalk = this.getById(id);
        if (imTalk.getUserId1().equals(UserContext.getCurrentUser().getId())) {
            imTalk.setDisable1(true);
            this.updateById(imTalk);
        } else if (imTalk.getUserId2().equals(UserContext.getCurrentUser().getId())) {
            imTalk.setDisable2(true);
            this.updateById(imTalk);
        }
    }

    @Override
    public List<ImTalkVO> getUserTalkList(IMTalkQueryParams imTalkQueryParams) {
        AuthUser authUser = UserContext.getCurrentUser();
        if (authUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        LambdaQueryWrapper<ImTalk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(wq -> wq.eq(ImTalk::getUserId1, authUser.getId()).or().eq(ImTalk::getUserId2, authUser.getId()));
        if (CharSequenceUtil.isNotEmpty(imTalkQueryParams.getUserName())) {
            queryWrapper.and(wq -> wq.ne(ImTalk::getUserId1, authUser.getId()).like(ImTalk::getName1, imTalkQueryParams.getUserName()).or().ne(ImTalk::getUserId2, authUser.getId()).like(ImTalk::getName2, imTalkQueryParams.getUserName()));
        }
        queryWrapper.orderByDesc(ImTalk::getLastTalkTime);
        List<ImTalk> imTalks = this.list(queryWrapper);

        Set<String> storeIds = new HashSet<>();
        for (ImTalk t : imTalks) {
            if (t.getStoreFlag1()) {
                storeIds.add(t.getUserId1());
            } else if (t.getStoreFlag2()) {
                storeIds.add(t.getUserId2());
            }
        }

        Set<String> memberIds = new HashSet<>();
        if (storeIds != null && !storeIds.isEmpty()) {
            memberIds = storeService.lambdaQuery()
                    .in(Store::getId, storeIds)
                    .select(Store::getMemberId)
                    .list()
                    .stream()
                    .map(Store::getMemberId)
                    .filter(Objects::nonNull)           // 防止 null 值
                    .collect(Collectors.toSet());
        }

        Map<String, Member> memberMap;
        if(memberIds!=null && memberIds.size()>0){
            memberMap = memberService.lambdaQuery()
                    .in(Member::getId, memberIds)
                    .select(Member::getStoreId, Member::getNickName, Member::getFace)
                    .list()
                    .stream()
                    .collect(Collectors.toMap(Member::getStoreId, m -> m));
        } else {
            memberMap = new HashMap<>();
        }


        List<ImTalkVO> imTalkVOList = imTalks.stream().map(imTalk -> {
            ImTalkVO imTalkVO = new ImTalkVO(imTalk, authUser.getId());
            if (imTalk.getStoreFlag1()) {
                Member opponentMember = memberMap.get(imTalk.getUserId1());
                if (opponentMember != null) {
                    String displayName = opponentMember.getNickName();
                    String displayFace = opponentMember.getFace();
                    imTalkVO.setName(displayName);
                    imTalkVO.setFace(displayFace);
                }
            } else if (imTalk.getStoreFlag2()) {
                Member opponentMember = memberMap.get(imTalk.getUserId2());
                if (opponentMember != null) {
                    String displayName = opponentMember.getNickName();
                    String displayFace = opponentMember.getFace();
                    imTalkVO.setName(displayName);
                    imTalkVO.setFace(displayFace);
                }
            }
            return imTalkVO;
        }).collect(Collectors.toList());
        getUnread(imTalkVOList);
        return imTalkVOList;
    }

    @Override
    public List<ImTalkVO> getStoreTalkList(IMTalkQueryParams imTalkQueryParams) {
        AuthUser authUser = UserContext.getCurrentUser();
        if (authUser == null) {
            throw new ServiceException(ResultCode.STORE_NOT_LOGIN_ERROR);
        }
        LambdaQueryWrapper<ImTalk> queryWrapper = new LambdaQueryWrapper<>();
        String storeId;
        if (authUser.getStoreId() == null) {
            Member member = memberService.getById(authUser.getId());
            if (member != null && member.getHaveStore() && member.getStoreId() != null) {
                storeId = member.getStoreId();
            } else {
                storeId = authUser.getStoreId();
            }
        } else {
            storeId = authUser.getStoreId();
        }
        queryWrapper.and(wq -> wq.eq(ImTalk::getUserId1, storeId).or().eq(ImTalk::getUserId2, storeId));
        if (CharSequenceUtil.isNotEmpty(imTalkQueryParams.getUserName())) {
            queryWrapper.and(wq -> wq.ne(ImTalk::getUserId1, storeId).like(ImTalk::getName1, imTalkQueryParams.getUserName()).or().ne(ImTalk::getUserId2, storeId).like(ImTalk::getName2, imTalkQueryParams.getUserName()));
        }
        queryWrapper.orderByDesc(ImTalk::getLastTalkTime);
        List<ImTalk> imTalks = this.list(queryWrapper);

        List<ImTalkVO> imTalkVOList = imTalks.stream().map(imTalk -> new ImTalkVO(imTalk, storeId)).collect(Collectors.toList());
        getUnread(imTalkVOList);
        return imTalkVOList;
    }

    @Override
    public String matchByUser() {
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }

        // 获取当前用户关联的店铺ID（如果有）
        String excludeStoreId = currentUser.getStoreId();

        if (excludeStoreId == null) {
            Member member = memberService.getById(currentUser.getId());
            if (member != null && member.getHaveStore() && member.getStoreId() != null) {
                excludeStoreId = member.getStoreId();
            }
        }

        // 兼容：商家角色但 storeId 为空时，从 Member 表再查一次
        if (CharSequenceUtil.isBlank(excludeStoreId)) {
            Member member = memberService.getById(currentUser.getId());
            if (member != null && Boolean.TRUE.equals(member.getHaveStore())
                    && CharSequenceUtil.isNotBlank(member.getStoreId())) {
                excludeStoreId = member.getStoreId();
            }
        }

        // 构建查询条件：不加 disable 和 open 限制，直接查询所有店铺
        LambdaQueryWrapper<Store> query = new LambdaQueryWrapper<>();
        query.eq(Store::getStoreDisable, StoreStatusEnum.OPEN.name());

        // 关键：排除自己的店铺（如果有关联店铺）
        boolean hasExclude = CharSequenceUtil.isNotBlank(excludeStoreId);
        if (hasExclude) {
            query.ne(Store::getId, excludeStoreId);
            log.info("用户 {} 有关联店铺 {}，匹配时已排除该店铺", currentUser.getId(), excludeStoreId);
        }

        // 获取排除后的店铺数量
        long totalAvailable = storeService.count(query);

        if (totalAvailable == 0) {
            if (hasExclude) {
                log.warn("排除用户自己的店铺后无其他店铺可匹配，用户ID: {}, 自己的店铺ID: {}",
                        currentUser.getId(), excludeStoreId);
            } else {
                log.warn("当前系统无任何店铺可匹配，用户ID: {}", currentUser.getId());
            }
            return null;  // 或抛出异常：throw new ServiceException("暂无其他商家可匹配");
        }

        // 生成随机偏移（0 ~ totalAvailable-1）
        long randomOffset = (long) (Math.random() * totalAvailable);

        // 添加 LIMIT 取一条（条件已包含排除）
        query.last("LIMIT " + randomOffset + ", 1");

        Store randomStore = storeService.getOne(query);

        if (randomStore == null) {
            log.error("随机偏移 {} 后未找到店铺，排除后总数: {}, 用户ID: {}",
                    randomOffset, totalAvailable, currentUser.getId());
            return null;
        }

        log.info("为用户 {} 随机匹配到店铺: {} ({})，排除店铺: {}, 排除后可用总数: {}",
                currentUser.getId(), randomStore.getId(), randomStore.getStoreName(),
                excludeStoreId, totalAvailable);

        return randomStore.getId();
    }

    /**
     * 获取未读消息数量
     *
     * @param imTalkVOList 消息列表
     */
    private void getUnread(List<ImTalkVO> imTalkVOList) {
        if (!imTalkVOList.isEmpty()) {
            for (ImTalkVO imTalkVO : imTalkVOList) {
                long count = imMessageService.count(new LambdaQueryWrapper<ImMessage>().eq(ImMessage::getFromUser, imTalkVO.getUserId()).eq(ImMessage::getTalkId, imTalkVO.getId()).eq(ImMessage::getIsRead, false));
                imTalkVO.setUnread(count);
            }
        }
    }
}