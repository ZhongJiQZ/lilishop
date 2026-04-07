package cn.lili.common.listener;

import cn.lili.common.event.TransactionCommitSendMQEvent;
import cn.lili.rocketmq.RocketmqSendCallbackBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 事务提交监听器
 *
 * @author paulG
 * @since 2022/1/19
 **/
@Component
@Slf4j
public class TransactionCommitSendMQListener {

    /**
     * rocketMq
     */
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(TransactionCommitSendMQEvent event) {
        String destination = event.getTopic() + ":" + event.getTag();
        String payloadPreview = summarizePayload(event.getMessage());
        log.info("[mq-producer] after_commit destination={} payloadType={} preview={} eventSource={}",
                destination,
                event.getMessage() != null ? event.getMessage().getClass().getSimpleName() : "null",
                payloadPreview,
                event.getSource());
        rocketMQTemplate.asyncSend(destination, event.getMessage(), RocketmqSendCallbackBuilder.commonCallback());
    }

    private static String summarizePayload(Object message) {
        if (message == null) {
            return "null";
        }
        String s = String.valueOf(message);
        int max = 512;
        if (s.length() <= max) {
            return s;
        }
        return s.substring(0, max) + "...(truncated,totalLen=" + s.length() + ")";
    }


}
