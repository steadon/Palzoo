package com.sipc.chatserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sipc.chatserver.exception.BusinessException;
import com.sipc.chatserver.mapper.MessageMapper;
import com.sipc.chatserver.mapper.RoomMapper;
import com.sipc.chatserver.mapper.RoomUserMergeMapper;
import com.sipc.chatserver.pojo.CommonResult;
import com.sipc.chatserver.pojo.domain.Message;
import com.sipc.chatserver.pojo.domain.Room;
import com.sipc.chatserver.pojo.domain.RoomUserMerge;
import com.sipc.chatserver.pojo.domain.User;
import com.sipc.chatserver.pojo.param.DeleteParam;
import com.sipc.chatserver.pojo.param.GetUserInfoParam;
import com.sipc.chatserver.pojo.param.NewMsgParam;
import com.sipc.chatserver.service.RoomService;
import com.sipc.chatserver.service.feign.LoginServer;
import com.sipc.chatserver.service.feign.TopicServer;
import com.sipc.chatserver.service.feign.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class RoomServiceImpl implements RoomService {
    private final UserServer userServer;
    private final RoomMapper roomMapper;
    private final LoginServer loginServer;
    private final TopicServer topicServer;
    private final MessageMapper messageMapper;
    private final RoomUserMergeMapper roomUserMergeMapper;

    @Autowired
    public RoomServiceImpl(UserServer userServer, RoomMapper roomMapper, LoginServer loginServer, TopicServer topicServer, MessageMapper messageMapper, RoomUserMergeMapper roomUserMergeMapper) {
        this.userServer = userServer;
        this.roomMapper = roomMapper;
        this.loginServer = loginServer;
        this.topicServer = topicServer;
        this.messageMapper = messageMapper;
        this.roomUserMergeMapper = roomUserMergeMapper;
    }

    @Override
    @Transactional
    public CommonResult<String> deleteRoom(Integer postId, String openid) {
        Room room = roomMapper.selectOne(new QueryWrapper<Room>().eq("post_id", postId));
        if (room == null) throw new BusinessException("不存在该聊天室");
        User user = loginServer.getUser(openid);
        if (!Objects.equals(topicServer.delete(new DeleteParam(user.getId(), postId)).getCode(), "00000"))
            throw new BusinessException("仅房主和管理员可删除帖子");
        roomMapper.deleteById(room);
        return CommonResult.success("解散聊天室成功");
    }

    @Override
    public List<NewMsgParam> lastMsg(Integer uid) {
        List<NewMsgParam> list = new LinkedList<>();
        for (RoomUserMerge roomUserMerge : roomUserMergeMapper.selectList(new QueryWrapper<RoomUserMerge>().eq("uid", uid))) {
            Integer roomId = roomUserMerge.getRoomId();
            Room room = roomMapper.selectOne(new QueryWrapper<Room>().eq("room_id", roomId));
            Integer postId = room.getPostId();
            Message message = messageMapper.selectOne(new QueryWrapper<Message>().orderByDesc("create_time"));
            GetUserInfoParam data = userServer.getUserInfo(uid).getData();
            list.add(new NewMsgParam(postId, data.getUsername(), message.getMessage()));
        }
        return list;
    }
}
