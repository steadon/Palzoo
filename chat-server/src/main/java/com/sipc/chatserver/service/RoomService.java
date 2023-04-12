package com.sipc.chatserver.service;

import com.sipc.chatserver.pojo.CommonResult;
import com.sipc.chatserver.pojo.param.NewMsgParam;

import java.util.List;

public interface RoomService {
    CommonResult<String> deleteRoom(Integer postId, String openid);

    List<NewMsgParam> lastMsg(Integer uid);
}
