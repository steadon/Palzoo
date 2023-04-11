package com.sipc.messageserver.rabbitmq;

import com.rabbitmq.client.Channel;
import com.sipc.messageserver.config.DirectRabbitConfig;
import com.sipc.messageserver.mapper.MessageMapper;
import com.sipc.messageserver.pojo.domain.Message;
import com.sipc.messageserver.pojo.dto.param.SendParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 11:26
 */
@Slf4j
@Component
public class MessageConsumer {

    @Resource
    private MessageMapper messageMapper;

    @RabbitListener(queues = DirectRabbitConfig.QUEUE_NAME, concurrency = "1", ackMode = "MANUAL")
    public void send(SendParam sendParam, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        Message message = new Message();

        message.setContent(sendParam.getContent());
        message.setUserId(sendParam.getUserId());
        message.setToUserId(sendParam.getToUserId());

        LocalDateTime now = LocalDateTime.now();

        message.setTime(now);
        message.setUpdatedTime(now);
        message.setCreatedTime(now);
        message.setIsDeleted((byte) 0);

        int insert = messageMapper.insert(message);
        if (insert != 1) {
            log.warn("发送消息接口异常，插入message数异常，插入数: {}，插入数据: {}", insert, message.toString());
        }

        log.info("发送消息成功，发送消息: {}", message.toString());

        channel.basicAck(deliveryTag, false);
    }

}
