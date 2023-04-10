package com.sipc.chatserver.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sipc.chatserver.mapper.RoomUserMergeMapper;
import com.sipc.chatserver.pojo.domain.RoomUserMerge;
import com.sipc.chatserver.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomUserMergeMapper roomUserMergeMapper;

    @Override
    public Boolean isUserInRoom(Integer uid, Integer roomId) {
        RoomUserMerge roomUserMerge = roomUserMergeMapper.selectOne(new QueryWrapper<RoomUserMerge>().eq("room_id", roomId).eq("uid", uid));
        return roomUserMerge != null;
    }
}
