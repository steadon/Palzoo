package com.sipc.chatserver.service;

import com.sipc.chatserver.pojo.CommonResult;

public interface RoomService {
    CommonResult<String> deleteRoom(Integer postId, String openid);
}
