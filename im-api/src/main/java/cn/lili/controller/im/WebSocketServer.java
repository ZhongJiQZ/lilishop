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
    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("accessToken") String accessToken, Session session) {

        AuthUser authUser = UserContext.getAuthUser(cache, accessToken);

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
        String sessionId = UserEnums.STORE.equals(authUser.getRole()) ? authUser.getStoreId() : authUser.getId();
        if (StrUtil.isNotBlank(userType)) {
            // 前端传了 userType，按显式意图处理
            if ("STORE".equalsIgnoreCase(userType)) {
                // 普通用户身份，但明确要用商家身份（典型：商家后台用会员账号登录）
                if(authUser.getStoreId()==null){
                    Member member = memberService.getById(authUser.getId());
                    if(member!=null && member.getHaveStore() && member.getStoreId()!=null){
                        sessionId = member.getStoreId();
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
                //保存消息
                ImMessage imMessage = new ImMessage(messageOperation);
                imMessageService.save(imMessage);
                //修改最后消息信息
                imTalkService.update(new LambdaUpdateWrapper<ImTalk>().eq(ImTalk::getId, messageOperation.getTalkId())
                    .set(ImTalk::getLastTalkMessage, messageOperation.getContext())
                    .set(ImTalk::getLastTalkTime, imMessage.getCreateTime())
                    .set(ImTalk::getLastMessageType, imMessage.getMessageType()));
                imMessage.setText(SensitiveWordsFilter.filter(imMessage.getText()));

                try {
                    // 发送消息消耗平台币（自动识别 to 是会员ID / 店铺ID）
                    String toId = messageOperation.getTo();
                    imMessageService.deductPlatformCoin(authUser.getId(), toId);
                } catch (ServiceException e) {
                    // 余额不足 → 返回提示，不发送消息
                    sendMessage(authUser.getId(), new MessageVO(MessageResultType.ERROR, e.getMessage()));
                    return;
                }

                //发送消息
                sendMessage(messageOperation.getTo(), new MessageVO(MessageResultType.MESSAGE, imMessage));
                // 关键：同时推给发送方和接收方
                String fromSessionId = messageOperation.getFrom();  // 店铺是 storeId，买家是 userId
                String toSessionId   = messageOperation.getTo();
                // 推给发送方所有端，实现发送方多端同步
                if (StrUtil.isNotBlank(fromSessionId) && !fromSessionId.equals(toSessionId)) {
                    sendMessage(fromSessionId, new MessageVO(MessageResultType.MESSAGE, imMessage));
                }
                break;
            case READ:
                if (StrUtil.isNotEmpty(messageOperation.getContext())) {
                    imMessageService.read(messageOperation.getTalkId(), accessToken);
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
