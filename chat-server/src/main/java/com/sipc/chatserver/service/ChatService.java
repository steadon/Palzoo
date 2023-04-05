package com.sipc.chatserver.service;

import com.sipc.chatserver.pojo.CommonResult;

public interface ChatService {

    CommonResult<String> sendMsg2Users(Integer roomId);

    CommonResult<String> sendMsg2Room(Integer uid, Integer roomId);

    CommonResult<String> listenAllMsg(Integer uid, Integer roomId);
}
