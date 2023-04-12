package com.sipc.userserver.service.feign;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.param.LoginServer.LevelParam;
import com.sipc.userserver.pojo.param.LoginServer.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "login-server")
public interface LoginServer {

    @GetMapping("/check/role")
    CommonResult<LevelParam> checkRole(@RequestParam(value = "openid") String openid);

    @GetMapping("/check/user")
    User getUser(@RequestParam(value = "openid") String openid);
}
