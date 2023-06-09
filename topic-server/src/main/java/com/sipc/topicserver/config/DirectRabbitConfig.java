package com.sipc.topicserver.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DirectRabbitConfig {

    public static final String QUEUE_NAME = "TopicRequestQueue"; //队列名称
    public static final String EXCHANGE_NAME = "TopicRequestExchange"; //交换器名称
    public static final String ROUTING_KEY = "TopicRequestRouting"; //路由键

    public static final String SUBMIT_DEAD_QUEUE_NAME = "SubmitRequestDeadQueue";
    public static final String SUBMIT_DEAD_EXCHANGE_NAME = "SubmitRequestDeadExchange";
    public static final String SUBMIT_DEAD_ROUTING_KEY = "SubmitRequestDeadRouting";

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);

        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //确认消息送到交换机(Exchange)回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("\n确认消息送到交换机(Exchange)结果：");
            System.out.println("相关数据：" + correlationData);
            System.out.println("是否成功：" + ack);
            System.out.println("错误原因：" + cause);
        });

        //确认消息送到队列(Queue)回调
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            System.out.println("\n确认消息送到队列(Queue)结果：");
            System.out.println("发生消息：" + returnedMessage.getMessage());
            System.out.println("回应码：" + returnedMessage.getReplyCode());
            System.out.println("回应信息：" + returnedMessage.getReplyText());
            System.out.println("交换机：" + returnedMessage.getExchange());
            System.out.println("路由键：" + returnedMessage.getRoutingKey());
            //指定一个死信队列消费消息
//            rabbitTemplate.convertAndSend(SUBMIT_DEAD_EXCHANGE_NAME, SUBMIT_DEAD_ROUTING_KEY, returnedMessage);
        });

        rabbitTemplate.setMessageConverter(messageConverter());

        return rabbitTemplate;
    }

    //队列 起名：NodeRequestQueue
    @Bean
    public Queue NodeRequestQueue() {
        /*
          创建队列，参数说明：
          String name：队列名称。
          boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
          持久化的队列会存盘，在服务器重启的时候不会丢失相关信息。
          boolean exclusive：设置是否排他，默认也是 false。为 true 则设置队列为排他。
          boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
          当没有生产者或者消费者使用此队列，该队列会自动删除。
          Map<String, Object> arguments：设置队列的其他一些参数。
         */

        return new Queue(QUEUE_NAME, true, false, false);
    }

    //Direct交换机 起名：NodeRequestQueue
    @Bean
    public DirectExchange NodeRequestExchange() {
        /*
          创建交换器，参数说明：
          String name：交换器名称
          boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
          持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。
          boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
         */

        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    //绑定
    @Bean
    public Binding bindingDirect() {
        //将队列和交换机绑定, 并设置用于匹配键：NodeRequestRouting
        return BindingBuilder.bind(NodeRequestQueue()).to(NodeRequestExchange()).with(ROUTING_KEY);
    }

    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange("lonelyExchange");
    }

    @Bean
    public DirectExchange deadRequestExchange() {
        return new DirectExchange(SUBMIT_DEAD_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue deadRequestQueue() {

        //设置死信队列参数
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", SUBMIT_DEAD_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key", SUBMIT_DEAD_ROUTING_KEY);

        return new Queue(SUBMIT_DEAD_QUEUE_NAME, true, false, false, arguments);
    }

    @Bean
    public Binding deadBindingDirect() {

        return BindingBuilder.bind(deadRequestQueue()).to(deadRequestExchange()).with(SUBMIT_DEAD_ROUTING_KEY);
    }
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new ();
//        factory.setConnectionFactory(connectionFactory);
//        //在消费者消费完成消息前，该消费者只接受一个消息
//        factory.setPrefetchCount(1);
//        factory.setDefaultRequeueRejected(true); // 设置重新加入队列
//        return factory;
//    }

}