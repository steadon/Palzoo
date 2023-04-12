package com.sipc.chatserver.controller;

import com.sipc.chatserver.pojo.CommonResult;
import com.sipc.chatserver.pojo.param.NewMsgParam;
import com.sipc.chatserver.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/room/delete")
    public CommonResult<String> deleteRoom(@RequestParam("postId") Integer postId, @RequestParam("openid") String openid) {
        return roomService.deleteRoom(postId, openid);
    }

    @GetMapping("/room/msg")
    public List<NewMsgParam> lastMsg(@RequestParam("uid") Integer uid) {
        return roomService.lastMsg(uid);
    }
}
