package com.sipc.messageserver.service.opfeign;

import com.sipc.messageserver.pojo.dto.result.chat.NewMsgParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("chat-server")
public interface ChatServer {

    @GetMapping("/room/msg")
    List<NewMsgParam> lastMsg(@RequestParam("uid") Integer uid);

}
