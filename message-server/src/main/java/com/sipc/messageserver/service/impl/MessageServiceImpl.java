package com.sipc.messageserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sipc.messageserver.config.DirectRabbitConfig;
import com.sipc.messageserver.mapper.MessageMapper;
import com.sipc.messageserver.pojo.domain.Message;
import com.sipc.messageserver.pojo.dto.CommonResult;
import com.sipc.messageserver.pojo.dto.mic.result.GetUserInfoResult;
import com.sipc.messageserver.pojo.dto.param.SendParam;
import com.sipc.messageserver.pojo.dto.result.MessageResult;
import com.sipc.messageserver.pojo.dto.result.chat.NewMsgParam;
import com.sipc.messageserver.pojo.dto.result.topicServer.DetailResult;
import com.sipc.messageserver.service.MessageService;
import com.sipc.messageserver.service.opfeign.ChatServer;
import com.sipc.messageserver.service.opfeign.TopicServer;
import com.sipc.messageserver.service.opfeign.UserServer;
import com.sipc.messageserver.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 11:03
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private UserServer userServer;

    @Resource
    private TopicServer topicServer;

    @Resource
    private ChatServer chatServer;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public CommonResult<String> send(SendParam sendParam) {
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(DirectRabbitConfig.EXCHANGE_NAME, DirectRabbitConfig.ROUTING_KEY, sendParam,
                //配置死信队列，消息过期时间5s
                message -> {
                    message.getMessageProperties().setExpiration("5000");
                    return message;
                }
        );

        return CommonResult.success("请求成功，发送中");
    }

    @Override
    public CommonResult<List<MessageResult>> getMessage(Integer userId) {

        List<Message> messages = messageMapper.selectList(new QueryWrapper<Message>().eq("to_user_id", userId).orderByDesc("id"));

        List<MessageResult> results = new ArrayList<>();

        for (Message message : messages) {
            MessageResult messageResult = new MessageResult();


            if (message.getUserId() == 0) {
                messageResult.setAuthorName("系统消息");
            } else {
                GetUserInfoResult author = getAuthor(message.getId());
                if (author == null || author.getUsername() == null) {
                    log.warn("用户信息查询有缺，查询用户id: {}", message.getUserId());
                    continue;
                }
                messageResult.setAuthorName(author.getUsername());
            }

            messageResult.setPostId(null);
            messageResult.setContent(message.getContent());
            messageResult.setUrl(null);

            results.add(messageResult);
        }

        List<NewMsgParam> newMsgParams;
        try {
            newMsgParams = chatServer.lastMsg(userId);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
            return CommonResult.success(results);
        }

        if (newMsgParams != null) {
            for (NewMsgParam newMsgParam : newMsgParams) {
                CommonResult<DetailResult> detail;
                try {
                    detail = topicServer.detail(newMsgParam.getPostId());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    log.warn(e.toString());
                    return CommonResult.success(results);
                }
                if (detail != null && Objects.equals(detail.getCode(), "00000")) {
                    if (detail.getData() != null) {

                        DetailResult detailResult = detail.getData();

                        MessageResult messageResult = new MessageResult();

                        messageResult.setPostId(newMsgParam.getPostId());
                        messageResult.setAuthorName(detailResult.getTitle());
                        messageResult.setContent(newMsgParam.getMsg());
//                        messageResult.setUrl(detailResult.getAuthor().getAvatarUrl());
                        if (detailResult.getAuthor() != null && detailResult.getAuthor().getAvatarUrl() != null) {
                            messageResult.setUrl(detailResult.getAuthor().getAvatarUrl());
                        } else {
                            messageResult.setUrl(null);
                        }

                        results.add(messageResult);

                    }
                }

            }
        }

        return CommonResult.success(results);
    }

    private GetUserInfoResult getAuthor(Integer authorId) {
        //redis获取用户信息
//        GetUserInfoResult author = (GetUserInfoResult)redisUtil.get("message:userId:" + authorId);
        GetUserInfoResult author;
        //如果redis中没有用户信息，使用openfeign调用user-server的服务获取用户详细信息
//        if (author == null ) {
            CommonResult<GetUserInfoResult> serverUserInfo = userServer.getUserInfo(authorId);
            if (Objects.equals(serverUserInfo.getCode(), "A0400")) {
                log.info("查询用户无信息，查询用户id {}", authorId);
                return null;
            }
            author = serverUserInfo.getData();
            if (author == null) {
                log.info("查询用户不存在，查询用户id {}", authorId);
                return null;
            }
//            redisUtil.set("message:userId:" + authorId, author);
//        }
        return author;
    }

}

