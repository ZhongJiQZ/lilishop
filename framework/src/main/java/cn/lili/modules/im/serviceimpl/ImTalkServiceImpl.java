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
    public ImTalkVO matchByUser() {
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }

        // 获取当前用户关联的店铺ID（如果有）
        String excludeStoreId = currentUser.getStoreId();
        if (CharSequenceUtil.isBlank(excludeStoreId)) {
            Member member = memberService.getById(currentUser.getId());
            if (member != null && Boolean.TRUE.equals(member.getHaveStore())
                    && CharSequenceUtil.isNotBlank(member.getStoreId())) {
                excludeStoreId = member.getStoreId();
            }
        }

        // 查询条件：正常营业的店铺，并排除自己（如果有关联店铺）
        LambdaQueryWrapper<Store> query = new LambdaQueryWrapper<>();
        query.eq(Store::getStoreDisable, StoreStatusEnum.OPEN.name());
        if (CharSequenceUtil.isNotBlank(excludeStoreId)) {
            query.ne(Store::getId, excludeStoreId);
        }

        long totalAvailable = storeService.count(query);
        if (totalAvailable == 0) {
            log.info("无可用店铺可匹配，当前用户: {}, 排除店铺: {}", currentUser.getId(), excludeStoreId);
            return null;
        }

        // 随机取一条
        long randomOffset = (long) (Math.random() * totalAvailable);
        query.last("LIMIT " + randomOffset + ", 1");

        Store randomStore = storeService.getOne(query);
        if (randomStore == null) {
            log.warn("随机偏移后未命中店铺，总数: {}, offset: {}", totalAvailable, randomOffset);
            return null;
        }

        // 重要：把店铺ID当作 userId 传给 getTalkByUser，内部会自动创建或获取 ImTalk
        String matchedUserId = randomStore.getId();  // 店铺ID 就是对方的 userId

        // 复用已有的 getTalkByUser 方法，它会创建对话（如果不存在）
        ImTalk imTalk = this.getTalkByUser(matchedUserId);
        if (imTalk == null) {
            log.error("匹配到店铺 {} 但创建/获取 ImTalk 失败", matchedUserId);
            return null;
        }

        // 转成 VO，使用当前用户视角
        ImTalkVO vo = new ImTalkVO(imTalk, currentUser.getId());

        // 关键：获取店铺对应的会员信息
        Member opponentMember = null;
        if (randomStore.getMemberId() != null) {
            opponentMember = memberService.getById(randomStore.getMemberId());
        }

        // 覆盖名称和头像，使用会员信息（如果存在）
        if (opponentMember != null) {
            // 优先使用会员昵称和头像
            vo.setName(opponentMember.getNickName());
            vo.setFace(opponentMember.getFace());

            // 可选：如果会员昵称为空，再 fallback 到店铺名
            if (CharSequenceUtil.isBlank(vo.getName())) {
                vo.setName(randomStore.getStoreName());
            }
            if (CharSequenceUtil.isBlank(vo.getFace())) {
                vo.setFace(randomStore.getStoreLogo());  // 假设 Store 有 storeLogo 字段
            }
        } else {
            // 如果店铺没有关联会员，fallback 到店铺信息
            vo.setName(randomStore.getStoreName());
            vo.setFace(randomStore.getStoreLogo());  // 根据实际字段调整
        }

        // 因为是新匹配的对话，通常 unread = 0，但可以强制刷新一次未读数（可选）
        // vo.setUnread(0L);  // 或调用
        getUnread(Collections.singletonList(vo));

        log.info("用户 {} 匹配到店铺 {} ({})，对话ID: {}",
                currentUser.getId(), randomStore.getId(), randomStore.getStoreName(), imTalk.getId());

        return vo;
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