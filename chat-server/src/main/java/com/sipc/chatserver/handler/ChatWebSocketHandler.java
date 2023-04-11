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
 * 基于ConcurrentHashMap和WebSocket实现的线程安全的聊天室
 *
 * @author Sterben
 * @version 1.0.0
 * @apiNote 由于session在redis中难以序列化，因此暂时使用ConcurrentHashMap存储会话信息
 */
@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static final Map<Integer, List<WebSocketSession>> rooms = new ConcurrentHashMap<>();
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

    /**
     * 建立连接后执行
     *
     * @param session 会话连接
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        try {
            OpenidAndPostId openidAndPostId = checkUrl(session);
            String postId = openidAndPostId.getPostId();
            String openid = openidAndPostId.getOpenid();

            //获取user和room
            User user = loginServer.getUser(openid);
            if (user == null) throw new BusinessException("[Websocket]Invalid user");
            Room room = roomMapper.selectOne(new QueryWrapper<Room>().eq("post_id", postId));
            if (room == null) {
                if (roomMapper.insert(new Room(postId)) == 0) throw new BusinessException("[Websocket]Insert failed");
                room = roomMapper.selectOne(new QueryWrapper<Room>().eq("post_id", postId));
            }

            //数据库记录用户进入聊天室信息
            if (roomUserMergeMapper.insert(new RoomUserMerge(user.getId(), room.getId())) == 0)
                throw new BusinessException("[Websocket]Insert failed");

            //内存中保存信息
            rooms.computeIfAbsent(room.getId(), k -> new CopyOnWriteArrayList<>()).add(session);

            //推送历史消息
            pushHistoryMsg(session, room.getId());

            //系统消息json化
            String time = LocalDateTime.now().format(formatter);
            String systemMsg = JSONObject.toJSONString(new SendMsg(time, user.getOpenid() + ",进入了聊天室", "system"));

            //广播用户加入的消息
            broadcast(room.getId(), systemMsg);
        } catch (Exception e) {
            log.error("[Websocket]Error occurred while establishing connection.", e);
        }
    }

    /**
     * 收到消息后执行
     *
     * @param session 会话连接
     * @param message 文本消息
     */
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        try {
            OpenidAndPostId openidAndPostId = checkUrl(session);
            String postId = openidAndPostId.getPostId();
            String openid = openidAndPostId.getOpenid();

            //获取room
            Room room = roomMapper.selectOne(new QueryWrapper<Room>().eq("post_id", postId));
            if (room == null) throw new BusinessException("[Websocket]Room is not exit");
            Integer roomId = room.getId();

            //获取消息
            String chatMsg = objectMapper.readValue(message.getPayload(), ChatMsg.class).getMessage();

            //数据库记录信息
            if (messageMapper.insert(new Message(openid, roomId, chatMsg)) == 0)
                throw new BusinessException("[Websocket]Lost msg:" + chatMsg);

            //用户消息json化
            String time = LocalDateTime.now().format(formatter);
            String msg = JSONObject.toJSONString(new SendMsg(time, chatMsg, openid));

            //广播新消息 - 排除用户本人
            broadcast(roomId, msg, openid);
        } catch (Exception e) {
            log.error("[Websocket]Error occurred while handling text message.", e);
        }
    }

    /**
     * 断开连接后执行
     *
     * @param session 会话连接
     * @param status  断开连接状态码
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        try {
            OpenidAndPostId openidAndPostId = checkUrl(session);
            String postId = openidAndPostId.getPostId();
            String openid = openidAndPostId.getOpenid();

            //获取user和room
            User user = loginServer.getUser(openid);
            Room room = roomMapper.selectOne(new QueryWrapper<Room>().eq("post_id", postId));
            if (user == null || room == null) throw new BusinessException("[Websocket]Invalid room or user");
            Integer userId = user.getId();
            Integer roomId = room.getId();

            //乐观删除离开用户的会话
            rooms.get(roomId).remove(session);

            //软删除该用户
            roomUserMergeMapper.selectList(new QueryWrapper<RoomUserMerge>().eq("uid", userId).eq("room_id", roomId)).forEach(a -> {
                a.setIsDeleted((byte) 1);
                roomUserMergeMapper.updateById(a);
            });

            //系统消息json化
            String time = LocalDateTime.now().format(formatter);
            String systemMsg = JSONObject.toJSONString(new SendMsg(time, openid + ",离开了聊天室", "system"));

            //广播用户离开的消息
            broadcast(roomId, systemMsg);
        } catch (Exception e) {
            log.error("[Websocket]Error occurred while closing connection.", e);
        }
    }

    /**
     * 广播给所有人消息
     *
     * @param roomId  帖子ID
     * @param message 要广播的消息
     */
    private void broadcast(Integer roomId, String message) {
        //遍历session并将消息发送给所有会话
        List<WebSocketSession> users = rooms.get(roomId);
        if (users != null) users.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("[Websocket]Error occurred while broadcasting message.", e);
            }
        });
    }

    /**
     * 广播给发送者以外消息
     *
     * @param roomId  帖子ID
     * @param message 要广播的消息
     * @param openid  该用户openid
     */
    private void broadcast(Integer roomId, String message, String openid) {
        //遍历session并将消息发送给非发送者以外的所有会话
        List<WebSocketSession> users = rooms.get(roomId);
        if (users != null) users.forEach(session -> {
            if (!checkUrl(session).getOpenid().equals(openid)) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    log.error("[Websocket]Error occurred while broadcasting message.", e);
                }
            }
        });
    }

    /**
     * 推送历史消息
     *
     * @param session 会话连接
     */
    private void pushHistoryMsg(WebSocketSession session, Integer roomId) {
        //遍历roomId相符合的message推送给该会话
        messageMapper.selectList(new QueryWrapper<Message>().eq("room_id", roomId)).forEach(msg -> {
            try {
                session.sendMessage(new TextMessage(JSONObject.toJSONString(new SendMsg(msg.getCreateTime().format(formatter), msg.getMessage(), msg.getOpenid()))));
            } catch (IOException e) {
                log.error("[Websocket]Error occurred while broadcasting message", e);
            }
        });
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
        if (openid == null || postId == null) throw new BusinessException("[Websocket]Params is null");
        return new OpenidAndPostId(openid, postId);
    }
}


