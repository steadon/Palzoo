package com.sipc.chatserver.service;

import com.sipc.chatserver.pojo.CommonResult;

public interface RoomService {

    Boolean isUserInRoom(Integer uid, Integer roomId);
}
