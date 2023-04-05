package com.sipc.chatserver.service.feign;

import com.sipc.chatserver.pojo.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "login-server")
public interface LoginServer {

    @GetMapping("check/user")
    User getUser(@RequestParam(value = "openid") String openid);
}
