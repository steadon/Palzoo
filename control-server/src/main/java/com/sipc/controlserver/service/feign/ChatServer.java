package com.sipc.controlserver.service.feign;

import com.sipc.controlserver.pojo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "chat-server")
public interface ChatServer {
    @GetMapping("/room/delete")
    CommonResult<String> deleteRoom(@RequestParam("postId") Integer postId, @RequestParam("openid") String openid);
}
