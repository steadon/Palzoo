package com.sipc.controlserver.service.feign;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.LevelParam;
import com.sipc.controlserver.pojo.param.OpenIdParam;
import com.sipc.controlserver.pojo.param.SignInParam;
import com.sipc.controlserver.pojo.param.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "login-server")
public interface LoginServer {

    @PostMapping("/sign/in")
    CommonResult<OpenIdParam> signIn(@RequestBody SignInParam param);

    @GetMapping("/sign/off")
    CommonResult<String> signOff(@RequestParam(value = "openid") String openid);

    @GetMapping("/check/role")
    CommonResult<LevelParam> checkRole(@RequestParam(value = "openid") String openid);

    @GetMapping("/check/user")
    User getUser(@RequestParam(value = "openid") String openid);
}
