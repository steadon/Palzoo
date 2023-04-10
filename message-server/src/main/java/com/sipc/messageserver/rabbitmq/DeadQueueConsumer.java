package com.sipc.messageserver.rabbitmq;

import com.sipc.messageserver.config.DirectRabbitConfig;
import com.sipc.messageserver.pojo.dto.param.SendParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 15:53
 */
@Slf4j
public class DeadQueueConsumer {

    @RabbitListener(queues = DirectRabbitConfig.MESSAGE_DEAD_QUEUE_NAME)
    public void consumer(SendParam sendParam) {
        log.error("发送消息请求失败, 消息参数为: {}", sendParam.toString());
    }

}
