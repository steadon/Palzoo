package com.sipc.chatserver.handler;

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
import com.sipc.chatserver.pojo.param.ChatMessage;
import com.sipc.chatserver.pojo.param.RoomAndUser;
import com.sipc.chatserver.service.feign.LoginServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@CrossOrigin
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, List<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final RoomMapper roomMapper;
    private final RoomUserMergeMapper roomUserMergeMapper;
    private final MessageMapper messageMapper;
    final private LoginServer loginServer;

    @Autowired
    public ChatWebSocketHandler(ObjectMapper objectMapper, RoomMapper roomMapper, RoomUserMergeMapper roomUserMergeMapper, MessageMapper messageMapper, LoginServer loginServer) {
        this.objectMapper = objectMapper;
        this.roomMapper = roomMapper;
        this.roomUserMergeMapper = roomUserMergeMapper;
        this.messageMapper = messageMapper;
        this.loginServer = loginServer;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        try {
            UriComponents uriComponents = UriComponentsBuilder.fromUri(Objects.requireNonNull(session.getUri())).build();
            String openid = uriComponents.getQueryParams().getFirst("openid");
            String postId = uriComponents.getQueryParams().getFirst("postId");
            //获取user和room
            User user = loginServer.getUser(openid);
            if (user == null) throw new BusinessException("非法用户");
            //数据库查找聊天室信息
            Room room = roomMapper.selectOne(new QueryWrapper<Room>().eq("post_id", postId).eq("is_deleted", 0));

            if (room == null) {
                if (roomMapper.insert(new Room(postId)) == 0) throw new BusinessException("创建聊天室失败");
                room = roomMapper.selectOne(new QueryWrapper<Room>().eq("post_id", postId).eq("is_deleted", 0));
            }
            //安全校验
            RoomUserMerge roomUserMerge = roomUserMergeMapper.selectOne(new QueryWrapper<RoomUserMerge>().eq("uid", user.getId()).eq("room_id", room.getId()).eq("is_deleted", 0));

            if (roomUserMerge != null) throw new BusinessException("非法进入聊天室");
            //数据库记录用户进入聊天室信息
            roomUserMergeMapper.insert(new RoomUserMerge(user.getId(), room.getId()));
            //内存中保存信息
            rooms.computeIfAbsent(postId, k -> new CopyOnWriteArrayList<>()).add(session);
            broadcast(postId, "用户 " + openid + " 进入了聊天室 " + postId);
        } catch (Exception e) {
            log.error("Error occurred while establishing connection.", e);
        }
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        try {
            UriComponents uriComponents = UriComponentsBuilder.fromUri(Objects.requireNonNull(session.getUri())).build();
            String openid = uriComponents.getQueryParams().getFirst("openid");
            String postId = uriComponents.getQueryParams().getFirst("postId");
            //获取user和room
            RoomAndUser inject = inject(openid, postId);
            //获取消息
            ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);
            //数据库记录信息
            messageMapper.insert(new Message(inject.getUser().getId(), inject.getRoom().getId(), chatMessage.getMessage()));
            broadcast(postId, openid + ": " + chatMessage.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while handling text message.", e);
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        try {
            UriComponents uriComponents = UriComponentsBuilder.fromUri(Objects.requireNonNull(session.getUri())).build();
            String openid = uriComponents.getQueryParams().getFirst("openid");
            String postId = uriComponents.getQueryParams().getFirst("postId");
            List<WebSocketSession> users = rooms.get(postId);
            if (users != null) {
                users.remove(session);
                //获取user和room
                RoomAndUser inject = inject(openid, postId);
                //删除改用户
                RoomUserMerge roomUserMerge = roomUserMergeMapper.selectOne(new QueryWrapper<RoomUserMerge>().eq("uid", inject.getUser().getId()).eq("room_id", inject.getRoom().getId()).eq("is_deleted", 0));

                roomUserMerge.setIsDeleted((byte) 1);
                roomUserMergeMapper.updateById(roomUserMerge);
                broadcast(postId, "用户 " + openid + " 离开了聊天室 " + postId);
            }
        } catch (Exception e) {
            log.error("Error occurred while closing connection.", e);
        }
    }

    private void broadcast(String roomId, String message) {
        List<WebSocketSession> users = rooms.get(roomId);
        if (users != null) {
            for (WebSocketSession session : users) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    log.error("Error occurred while broadcasting message.", e);
                }
            }
        }
    }

    private RoomAndUser inject(String openid, String postId) {
        User user = loginServer.getUser(openid);
        //数据库查找聊天室信息
        Room room = roomMapper.selectOne(new QueryWrapper<Room>().eq("post_id", postId).eq("is_deleted", 0));
        if (room == null && user == null) throw new BusinessException("非法聊天");
        return new RoomAndUser(user, room);
    }
}


