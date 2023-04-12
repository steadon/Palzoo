package com.sipc.topicserver.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.client.Channel;
import com.sipc.topicserver.config.DirectRabbitConfig;
import com.sipc.topicserver.constant.Constant;
import com.sipc.topicserver.mapper.CategoryMapper;
import com.sipc.topicserver.mapper.PostMapper;
import com.sipc.topicserver.pojo.domain.Category;
import com.sipc.topicserver.pojo.domain.Post;
import com.sipc.topicserver.pojo.dto.param.SubmitParam;
import com.sipc.topicserver.pojo.dto.param.messageServer.SendParam;
import com.sipc.topicserver.service.openfeign.MessageServer;
import com.sipc.topicserver.util.RedisUtil;
import com.sipc.topicserver.util.ac.ACResult;
import com.sipc.topicserver.util.ac.ACUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author o3141
 * @since 2023/4/4 18:59
 * @version 1.0
 */
@Component
@Slf4j
public class SubmitConsumer {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private PostMapper postMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private MessageServer messageServer;

    @Resource
    private ACUtil acUtil;

    @RabbitListener(queues = DirectRabbitConfig.QUEUE_NAME, concurrency = "1")
    public void consumer(SubmitParam submitParam) {

        //获取分类id
        Integer categoryId = (Integer)redisUtil.get("categoryName:" + submitParam.getCategory());

        if (categoryId == null) {
            Category category1 = categoryMapper.selectOne(
                    new QueryWrapper<Category>()
                            .eq("name", submitParam.getCategory())
                            .last("limit 1")
            );
            if (category1 == null) {
                log.warn("未查到正确的分类id，查询分类名称： {}", submitParam.getCategory());

                SendParam sendParam = new SendParam();
                sendParam.setUserId(0);
                sendParam.setToUserId(submitParam.getUserId());
                sendParam.setContent("分类错误，提交失败");

                //捕获异常，调用openfeign发送消息可能导致异常导致消息被重复消费
                try {
                    messageServer.send(sendParam);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("发送消息异常：{}", e.toString());
                }
                return;
            }
            categoryId = category1.getId();
            redisUtil.set("categoryName:" + submitParam.getCategory(), categoryId);
        }

        //检验敏感词
        ACResult check = acUtil.check(submitParam.getTitle(), submitParam.getContext());
        if (check != null && check.getStatusCode() == 0 ) {
            if (check.getTitleList() != null || check.getContentList() != null) {

//                StringBuilder stringBuilder = new StringBuilder();
//
//                stringBuilder.append("您的帖子含有违禁词语，请您修正。</br>违禁词语如下：</br>");
//
//                if (check.getTitleList() != null) {
//                    stringBuilder.append("标题：");
//                    stringBuilder.append(check.getTitleList());
//                    stringBuilder.append("</br>");
//                }
//
//                if (check.getContentList() != null) {
//                    stringBuilder.append("文本：");
//                    stringBuilder.append(check.getContentList());
//                    stringBuilder.append("</br>");
//                }

                SendParam sendParam = new SendParam();

                sendParam.setUserId(0);
                sendParam.setToUserId(submitParam.getUserId());
                sendParam.setContent("您的帖子含有违禁词语，请您修正。");

                //捕获异常，调用openfeign发送消息可能导致异常导致消息被重复消费
                try {
                    messageServer.send(sendParam);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("发送消息异常：{}", e.toString());
                }
                return;
            }
        }


        //获取子标签id
        Integer authorId = submitParam.getUserId();

        Post post = new Post();

        //设置post类数据
        post.setTitle(submitParam.getTitle());
        post.setContent(submitParam.getContext());
        post.setAuthorId(authorId);
        post.setCategoryId(categoryId);

        //获取标签
        StringBuilder categoryNext = new StringBuilder();
        int i = 1;
        if (submitParam.getCategoryNext() != null) {
            int len = submitParam.getCategoryNext().size();
            for (String s : submitParam.getCategoryNext()) {
                if (i < len) {
                    categoryNext.append(s).append("+");
                } else {
                    categoryNext.append(s);
                }
                ++i;
            }
        }

        post.setCategoryNext(categoryNext.toString());
        post.setGender(submitParam.getGender());
        post.setNumber(submitParam.getNumber());
        post.setIsFinish((byte) 0);
        post.setGoTime(LocalDateTime.parse(submitParam.getGoTime(), Constant.dateTimeFormatter));
        post.setWatchNum(0);
        post.setUpdatedTime(LocalDateTime.now());
        post.setCreatedTime(LocalDateTime.now());
        post.setIsDeleted((byte) 0);

        //插入数据
        int insert = postMapper.insert(post);

        if (insert != 1) {
            log.error("数据库操作异常，帖子提交操作失败，插入数据数：{}, 插入帖子为： {}", insert, post);

            SendParam sendParam = new SendParam();
            sendParam.setUserId(0);
            sendParam.setToUserId(submitParam.getUserId());
            sendParam.setContent("提交失败");

//            channel.basicAck(deliveryTag, false);
            //捕获异常，调用openfeign发送消息可能导致异常导致消息被重复消费
            try {
                messageServer.send(sendParam);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("发送消息异常：{}", e.toString());
            }
            return;
        }

        //发送消息
        SendParam sendParam = new SendParam();
        sendParam.setUserId(0);
        sendParam.setToUserId(submitParam.getUserId());
        sendParam.setContent(submitParam.getTitle() + "提交成功");

        try {
            messageServer.send(sendParam);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("发送消息异常：{}", e.toString());
        }
    }
}
