package com.sipc.chatserver.handler;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipc.chatserver.exception.BusinessException;
import com.sipc.chatserver.mapper.MessageMapper;
import com.sipc.chatserver.mapper.RoomMapper;
import com.sipc.chatserver.mapper.RoomUserMergeMapper;
import com.sipc.chatserver.pojo.domain.Message;
import com.sipc.chatserver.pojo.domain.Room;
import com.sipc.chatserver.pojo.domain.RoomUserMerge;
import com.sipc.chatserver.pojo.domain.User;
import com.sipc.chatserver.pojo.param.ChatMsg;
import com.sipc.chatserver.pojo.param.OpenidAndPostId;
import com.sipc.chatserver.pojo.param.RoomAndUser;
import com.sipc.chatserver.pojo.param.SendMsg;
import com.sipc.chatserver.service.feign.LoginServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Sterben
 * <p>
 * 基于ComcurrentHashMap和WebSocket实现的线程安全的聊天室
 * @version 1.0
 */
@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static final Map<String, List<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final RoomUserMergeMapper roomUserMergeMapper;
    private final MessageMapper messageMapper;
    private final ObjectMapper objectMapper;
    private final LoginServer loginServer;
    private final RoomMapper roomMapper;

    @Autowired
    public ChatWebSocketHandler(ObjectMapper objectMapper, RoomMapper roomMapper, RoomUserMergeMapper roomUserMergeMapper, MessageMapper messageMapper, LoginServer loginServer) {
        this.roomUserMergeMapper = roomUserMergeMapper;
        this.messageMapper = messageMapper;
        this.objectMapper = objectMapper;
        this.loginServer = loginServer;
        this.roomMapper = roomMapper;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        try {
            OpenidAndPostId openidAndPostId = checkUrl(session);
            String postId = openidAndPostId.getPostId();
            String openid = openidAndPostId.getOpenid();

            //获取user和room
            User user = loginServer.getUser(openid);
            if (user == null) throw new BusinessException("非法用户");
            Room room = roomMapper.selectOne(new QueryWrapper<Room>().eq("post_id", postId));
            if (room == null) {
                if (roomMapper.insert(new Room(postId)) == 0) throw new BusinessException("创建聊天室失败");
                room = roomMapper.selectOne(new QueryWrapper<Room>().eq("post_id", postId));
            }

            //数据库记录用户进入聊天室信息
            roomUserMergeMapper.insert(new RoomUserMerge(user.getId(), room.getId()));

            //内存中保存信息
            rooms.computeIfAbsent(postId, k -> new CopyOnWriteArrayList<>()).add(session);

            //推送历史消息
            pushHistoryMsg(session, Integer.valueOf(postId));

            //系统消息json化
            String time = LocalDateTime.now().format(formatter);
            String systemMsg = JSONObject.toJSONString(new SendMsg(time, user.getOpenid() + ",进入了聊天室", "system"));

            //广播用户加入的消息
            broadcast(postId, systemMsg);
        } catch (Exception e) {
            log.error("Error occurred while establishing connection.", e);
        }
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        try {
            OpenidAndPostId openidAndPostId = checkUrl(session);
            String postId = openidAndPostId.getPostId();
            String openid = openidAndPostId.getOpenid();

            //获取user和room
            RoomAndUser inject = loadParam(openid, postId);

            //获取消息
            ChatMsg chatMsg = objectMapper.readValue(message.getPayload(), ChatMsg.class);

            //数据库记录信息
            messageMapper.insert(new Message(inject.getUser().getOpenid(), inject.getRoom().getId(), chatMsg.getMessage()));

            //时间戳格式化
            String time = LocalDateTime.now().format(formatter);

            //广播新消息 - 排除用户本人
            broadcast(postId, new SendMsg(time, chatMsg.getMessage(), inject.getUser().getOpenid()));
        } catch (Exception e) {
            log.error("Error occurred while handling text message.", e);
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        try {
            OpenidAndPostId openidAndPostId = checkUrl(session);
            String postId = openidAndPostId.getPostId();
            String openid = openidAndPostId.getOpenid();

            //获取user和room
            RoomAndUser inject = loadParam(openid, postId);

            //乐观删除离开用户的会话
            List<WebSocketSession> users = rooms.get(postId);
            users.remove(session);

            //软删除该用户
            List<RoomUserMerge> roomUserMerges = roomUserMergeMapper.selectList(new QueryWrapper<RoomUserMerge>().eq("uid", inject.getUser().getId()).eq("room_id", inject.getRoom().getId()));
            roomUserMerges.forEach(a -> {
                a.setIsDeleted((byte) 1);
                roomUserMergeMapper.updateById(a);
            });

            //系统消息json化
            String time = LocalDateTime.now().format(formatter);
            String systemMsg = JSONObject.toJSONString(new SendMsg(time, inject.getUser().getOpenid() + ",离开了聊天室", "system"));

            //广播用户离开的消息
            broadcast(postId, systemMsg);
        } catch (Exception e) {
            log.error("Error occurred while closing connection.", e);
        }
    }

    /**
     * 广播给所有人消息
     *
     * @param postId  帖子ID
     * @param message 要广播的消息
     */
    private void broadcast(String postId, String message) {
        List<WebSocketSession> users = rooms.get(postId);
        if (users != null) {
            for (WebSocketSession session : users) {
                //对该组内的用户广播消息对象
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    log.error("Error occurred while broadcasting message.", e);
                }
            }
        }
    }

    /**
     * 广播给发送者以外消息
     *
     * @param postId  帖子ID
     * @param message 要广播的消息对象
     */
    private void broadcast(String postId, SendMsg message) {
        List<WebSocketSession> users = rooms.get(postId);
        if (users != null) {
            for (WebSocketSession session : users) {
                //对该组内的用户广播消息对象
                try {
                    //广播给发送者以外的所有用户
                    if (!Objects.equals(checkUrl(session).getOpenid(), message.getOpenid()))
                        session.sendMessage(new TextMessage(JSONObject.toJSONString(message)));
                } catch (IOException e) {
                    log.error("Error occurred while broadcasting message.", e);
                }
            }
        }
    }

    /**
     * 推送历史消息
     *
     * @param session 会话
     */
    private void pushHistoryMsg(WebSocketSession session, Integer postId) {
        List<Message> messages = messageMapper.selectList(null);
        try {
            for (Message message : messages) {
                //读取对应房间的历史消息
                if (message.getRoomId().equals(postId)) {
                    //读取用户openid
                    String openid = message.getOpenid();
                    //用户消息json化
                    String time = message.getCreateTime().format(formatter);
                    String userMsg = JSONObject.toJSONString(new SendMsg(time, message.getMessage(), openid));
                    session.sendMessage(new TextMessage(userMsg));
                }
            }
        } catch (IOException e) {
            log.error("Error occurred while broadcasting message.", e);
        }
    }

    /**
     * 远程调用获取user对象
     * 数据库查询room对象
     *
     * @param openid 微信用户唯一标识
     * @param postId 帖子唯一标识
     * @return 返回 room 和 user 对象
     */

    private RoomAndUser loadParam(String openid, String postId) {
        //feign 远程调用 login-server 获取 user 对象
        User user = loginServer.getUser(openid);
        //数据库查找聊天室信息
        Room room = roomMapper.selectOne(new QueryWrapper<Room>().eq("post_id", postId));
        if (room == null && user == null) throw new BusinessException("非法聊天");
        return new RoomAndUser(user, room);
    }

    /**
     * 从url中获取参数
     *
     * @param session 会话对象
     * @return 返回 openid 和 postId
     */
    private OpenidAndPostId checkUrl(WebSocketSession session) {
        UriComponents uriComponents = UriComponentsBuilder.fromUri(Objects.requireNonNull(session.getUri())).build();
        String openid = uriComponents.getQueryParams().getFirst("openid");
        String postId = uriComponents.getQueryParams().getFirst("postId");
        if (openid == null || postId == null) throw new BusinessException("参数为空");
        return new OpenidAndPostId(openid, postId);
    }
}


