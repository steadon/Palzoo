package com.sipc.chatserver.service.impl;

import com.sipc.chatserver.pojo.CommonResult;
import com.sipc.chatserver.service.ChatService;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    @Override
    public CommonResult<String> sendMsg2Users(Integer roomId) {
        return null;
    }

    @Override
    public CommonResult<String> sendMsg2Room(Integer uid, Integer roomId) {
        return null;
    }

    @Override
    public CommonResult<String> listenAllMsg(Integer uid, Integer roomId) {
        return null;
    }
}
