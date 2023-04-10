package com.sipc.chatserver.controller;

import com.sipc.chatserver.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RoomController {
    @Autowired
    private RoomService roomService;

    public Boolean isUserInRoom(Integer uid, Integer roomId) {
        log.info("isUserInRoom被远程调用");
        return roomService.isUserInRoom(uid, roomId);
    }
}
