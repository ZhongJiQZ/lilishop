package cn.lili.controller.im;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.lili.cache.Cache;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.sensitive.SensitiveWordsFilter;
import cn.lili.modules.im.config.CustomSpringConfigurator;
import cn.lili.modules.im.entity.dos.ImMessage;
import cn.lili.modules.im.entity.dos.ImTalk;
import cn.lili.modules.im.entity.enums.MessageResultType;
import cn.lili.modules.im.entity.vo.MessageOperation;
import cn.lili.modules.im.entity.vo.MessageVO;
import cn.lili.modules.im.service.ImMessageService;
import cn.lili.modules.im.service.ImTalkService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.permission.entity.dos.AdminUser;
import cn.lili.modules.permission.service.AdminUserService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author liushuai
 */
@Component
@ServerEndpoint(value = "/lili/webSocket/{accessToken}", configurator = CustomSpringConfigurator.class)
@Scope("prototype")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSocketServer {
    /**
     * 在线人数 PS 注意，只能单节点，如果多节点部署需要自行寻找方案
     */
//    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();
    // 修改为：一个 sessionId 可以对应多个 Session（多端在线）
    private static ConcurrentHashMap<String, CopyOnWriteArraySet<Session>> sessionPools = new ConcurrentHashMap<>();


    /**
     * 消息服务
     */
    private final ImMessageService imMessageService;
    private final ImTalkService imTalkService;
    private final Cache cache;

    @Autowired
    private MemberService memberService;
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("accessToken") String accessToken, Session session) {

        AuthUser authUser = UserContext.getAuthUser(cache, accessToken);

        // 角色存入 session
        if (authUser != null && authUser.getRole() != null) {
            session.getUserProperties().put("role", authUser.getRole().name());
        }

        String query = session.getQueryString();
        String userType = null;

        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("userType=")) {
                    userType = param.substring("userType=".length());
                    break;
                }
            }
        }

        // 核心修改：sessionId 计算逻辑
        String sessionId;
        if(UserEnums.MANAGER.equals(authUser.getRole())){
            sessionId = authUser.getId();
        } else {
            sessionId = UserEnums.STORE.equals(authUser.getRole()) ? authUser.getStoreId() : authUser.getId();
            if (StrUtil.isNotBlank(userType)) {
                // 前端传了 userType，按显式意图处理
                if ("STORE".equalsIgnoreCase(userType)) {
                    // 普通用户身份，但明确要用商家身份（典型：商家后台用会员账号登录）
                    if (authUser.getStoreId() == null) {
                        Member member = memberService.getById(authUser.getId());
                        if (member != null && member.getHaveStore() && member.getStoreId() != null) {
                            sessionId = member.getStoreId();
                        }
                    }
                }
            }
        }

        //如果已有会话，则进行下线提醒。
//        if (sessionPools.containsKey(sessionId)) {
//            log.info("用户重复登陆，旧用户下线");
//            Session oldSession = sessionPools.get(sessionId);
//            sendMessage(oldSession,
//                MessageVO.builder().messageResultType(MessageResultType.OFFLINE).result("用户异地登陆").build());
//            try {
//                oldSession.close();
//            } catch (Exception e) {
//                log.error("关闭旧会话异常", e);
//            }
//        }
//        sessionPools.put(sessionId, session);
//        log.info("用户建立连接，sessionId: {}，userType: {}", sessionId, userType);

        // 获取或创建该 sessionId 的 Session 集合
        CopyOnWriteArraySet<Session> sessions = sessionPools.computeIfAbsent(sessionId,
                k -> new CopyOnWriteArraySet<>());

        // 添加当前 session（不再踢旧的）
        sessions.add(session);

        // 存储 sessionId 到 session 属性（用于 onClose）
        session.getUserProperties().put("sessionId", sessionId);

        log.info("用户建立连接，sessionId: {}, 当前该身份在线端数: {}, userType: {}",
                sessionId, sessions.size(), userType);
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(@PathParam("accessToken") String accessToken, Session session) {
//        AuthUser authUser = UserContext.getAuthUser(cache, accessToken);
//        String sessionId = UserEnums.STORE.equals(authUser.getRole()) ? authUser.getStoreId() : authUser.getId();
//        log.info("用户断开连接:{}", JSONUtil.toJsonStr(authUser));
//        sessionPools.remove(sessionId);

        String sessionId = (String) session.getUserProperties().get("sessionId");
        if (StrUtil.isBlank(sessionId)) {
            log.warn("onClose 时未找到 sessionId，session id: {}", session.getId());
            return;
        }

        CopyOnWriteArraySet<Session> sessions = sessionPools.get(sessionId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                sessionPools.remove(sessionId);
            }
            log.info("用户断开连接，sessionId: {}, 剩余在线端数: {}", sessionId, sessions.size());
        }
    }

    /**
     * 发送消息
     *
     * @param msg
     * @throws IOException
     */
    @OnMessage
    public void onMessage(@PathParam("accessToken") String accessToken, String msg) {
        log.info("发送消息：{}", msg);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MessageOperation messageOperation = objectMapper.readValue(msg, MessageOperation.class);
            operation(accessToken, messageOperation);
        } catch (Exception e) {
            log.error("消息解析失败: {}", msg, e);
        }
    }

    /**
     * IM操作
     *
     * @param accessToken
     * @param messageOperation
     */
    private void operation(String accessToken, MessageOperation messageOperation) {

        AuthUser authUser = UserContext.getAuthUser(cache, accessToken);
        switch (messageOperation.getOperationType()) {
            case PING:
                break;
            case MESSAGE:
                ImMessage imMessage = new ImMessage(messageOperation);
                AdminUser adminUser = null;
                Member fromMember = null;
                Member toMember = null;
                String fromId = "";
                try {
                    // 发送消息消耗平台币（自动识别 to 是会员ID / 店铺ID）
                    fromId = messageOperation.getFrom();
                    String toId = messageOperation.getTo();
                    adminUser = adminUserService.getById(fromId);
                    fromMember = memberService.getById(fromId);
                    toMember = memberService.getById(toId);
                    log.error("发送人：{}, 接收人：{}, 管理员：{}", fromId, toId, adminUser != null?JSON.toJSONString(adminUser):"为空");
                    imMessageService.deductPlatformCoin(fromId, toId, adminUser);
                } catch (ServiceException e) {
                    // 余额不足 → 返回提示，不发送消息
                    log.error("余额不足 → 返回提示，不发送消息：{}", e.getMsg());
                    imMessage.setText(e.getMsg());
                    sendMessage(authUser.getId(), new MessageVO(MessageResultType.ERROR, imMessage));
                    return;
                }
                String fromSessionId = messageOperation.getFrom();  // 店铺是 storeId，买家是 userId
                if(adminUser == null) {
                    // 转发给管理员
                    forwardToAllOnlineManagers(imMessage);
                } else {
                    // 转发给试穿员
                    ImTalk talk = imTalkService.getById(messageOperation.getTalkId());
                    if(talk != null) {
                        if (Boolean.TRUE.equals(talk.getStoreFlag1())) {
                            imMessage.setFromUser(talk.getUserId1());
                            fromSessionId = talk.getUserId1();
                        } else if (Boolean.TRUE.equals(talk.getStoreFlag2())) {
                            imMessage.setFromUser(talk.getUserId2());
                            fromSessionId = talk.getUserId2();
                        }
                    }
                    // 转发给非自己的管理员
                    forwardToAllOnlineManagers(imMessage);
                }
                //保存消息
                imMessageService.save(imMessage);
                //修改最后消息信息
                imTalkService.update(new LambdaUpdateWrapper<ImTalk>().eq(ImTalk::getId, messageOperation.getTalkId())
                    .set(ImTalk::getLastTalkMessage, messageOperation.getContext())
                    .set(ImTalk::getLastTalkTime, imMessage.getCreateTime())
                    .set(ImTalk::getLastMessageType, imMessage.getMessageType()));
                imMessage.setText(SensitiveWordsFilter.filter(imMessage.getText()));

                //发送消息
                sendMessage(messageOperation.getTo(), new MessageVO(MessageResultType.MESSAGE, imMessage));
                //转发给试穿员(试穿员给试穿员)
                if(fromMember != null && fromMember.getHaveStore() && fromMember.getStoreId() != null && sessionPools.containsKey(fromMember.getStoreId())) {
                    sendMessage(fromMember.getStoreId(), new MessageVO(MessageResultType.MESSAGE, imMessage));
                }
                //转发给试穿员(试穿员给试穿员)
                if(toMember != null && toMember.getHaveStore() && toMember.getStoreId() != null && sessionPools.containsKey(toMember.getStoreId())) {
                    sendMessage(toMember.getStoreId(), new MessageVO(MessageResultType.MESSAGE, imMessage));
                }
                // 关键：同时推给发送方和接收方
//                String fromSessionId = messageOperation.getFrom();  // 店铺是 storeId，买家是 userId
                String toSessionId   = messageOperation.getTo();
                // 推给发送方所有端，实现发送方多端同步
                if (StrUtil.isNotBlank(fromSessionId) && !fromSessionId.equals(toSessionId)) {
                    sendMessage(fromSessionId, new MessageVO(MessageResultType.MESSAGE, imMessage));
                }

                // 给【接收消息的人】推送最新未读数量
                sendMessage(messageOperation.getTo(),
                        new MessageVO(MessageResultType.UN_READ, imMessageService.getUnreadCount(messageOperation.getTo())));
                break;
            case READ:
                if (StrUtil.isNotEmpty(messageOperation.getTalkId())) {
                    imMessageService.read(messageOperation.getTalkId(), accessToken);
                } else {
                    log.warn("READ操作缺少talkId，忽略本次已读请求");
                }
                break;
            case UNREAD:
                sendMessage(authUser.getId(),
                    new MessageVO(MessageResultType.UN_READ, imMessageService.unReadMessages(accessToken)));
                break;
            case HISTORY:
                sendMessage(authUser.getId(), new MessageVO(MessageResultType.HISTORY,
                    imMessageService.historyMessage(accessToken, messageOperation.getTo())));
                break;
            default:
                break;
        }
    }

    /**
     * 【监控】将消息转发给所有在线管理员（去重版，避免多端重复）
     * @param imMessage 要转发的消息
     */
    private void forwardToAllOnlineManagers(ImMessage imMessage) {
        try {
            // 1. 先收集所有唯一的管理员 sessionId（去重，避免同一个账号多次处理）
            Set<String> managerSessionIds = new HashSet<>();
            for (String sessionId : sessionPools.keySet()) {
                CopyOnWriteArraySet<Session> sessions = sessionPools.get(sessionId);
                if (sessions == null || sessions.isEmpty()) {
                    continue;
                }
                // 只要该 sessionId 下有一个端是管理员，就标记为管理员 sessionId
                boolean isManager = sessions.stream()
                        .anyMatch(s -> UserEnums.MANAGER.name().equals(s.getUserProperties().get("role")));
                if (isManager) {
                    managerSessionIds.add(sessionId);
                }
            }

            // 2. 遍历唯一的管理员 sessionId，给每个 sessionId 下的所有端广播一次
            MessageVO messageVO = new MessageVO(MessageResultType.MESSAGE, imMessage);
            for (String sessionId : managerSessionIds) {
                sendMessage(sessionId, messageVO);
                log.info("【聊天监控】已转发消息给管理员 sessionId: {}", sessionId);
            }
        } catch (Exception e) {
            log.error("【聊天监控】转发消息给管理员失败", e);
        }
    }

    /**
     * 发送消息
     *
     * @param sessionId sessionId
     * @param message   消息对象
     */
    private void sendMessage(String sessionId, MessageVO message) {
//        Session session = sessionPools.get(sessionId);
//        sendMessage(session, message);

        CopyOnWriteArraySet<Session> sessions = sessionPools.get(sessionId);
        if (sessions != null && !sessions.isEmpty()) {
            String text = JSONUtil.toJsonStr(message);
            for (Session s : sessions) {
                if (s.isOpen()) {
                    try {
                        s.getBasicRemote().sendText(text);
                    } catch (Exception e) {
                        log.error("向 sessionId:{} 的某个端发送失败", sessionId, e);
                        // 可选：发送失败移除该 session
                        sessions.remove(s);
                    }
                } else {
                    sessions.remove(s);
                }
            }
            log.info("消息广播给 sessionId: {}, 在线端数: {}", sessionId, sessions.size());
        } else {
            log.info("sessionId: {} 当前无在线端，消息暂不推送", sessionId);
        }
    }

    /**
     * 发送消息
     *
     * @param session 会话
     * @param message 消息对象
     */
    private void sendMessage(Session session, MessageVO message) {
        if (session != null) {
            try {
                session.getBasicRemote().sendText(JSONUtil.toJsonStr(message));
            } catch (Exception e) {
                log.error("发送消息异常", e);
            }
        }
    }

    /**
     * socket exception
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("socket异常: {}", session.getId(), throwable);
    }

}
