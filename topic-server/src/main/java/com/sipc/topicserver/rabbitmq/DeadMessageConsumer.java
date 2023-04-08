package com.sipc.topicserver.rabbitmq;

import com.sipc.topicserver.config.DirectRabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/6 19:21
 */
@Component
@Slf4j
public class DeadMessageConsumer {

    @RabbitListener(queues = DirectRabbitConfig.SUBMIT_DEAD_QUEUE_NAME)
    public void consumer(Object submitParam) {

        log.warn("死信队列消息，来自帖子提交接口，提交错误信息为： {}", submitParam.toString());

    }
}
