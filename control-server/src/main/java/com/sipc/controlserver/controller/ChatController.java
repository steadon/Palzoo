package com.sipc.controlserver.controller;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.service.feign.ChatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class ChatController {

    private final ChatServer chatServer;

    @Autowired
    public ChatController(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    @GetMapping("/room/delete")
    public CommonResult<String> deleteRoom(@RequestParam("postId") Integer postId, @RequestParam("openid") String openid) {
        return chatServer.deleteRoom(postId, openid);
    }
}
