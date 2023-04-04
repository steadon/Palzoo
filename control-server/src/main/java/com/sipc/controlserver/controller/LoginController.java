package com.sipc.controlserver.controller;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.LevelParam;
import com.sipc.controlserver.pojo.param.OpenIdParam;
import com.sipc.controlserver.pojo.param.SignInParam;
import com.sipc.controlserver.service.feign.LoginServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("control")
public class LoginController {

    private final LoginServer loginServer;

    @Autowired
    public LoginController(LoginServer loginServer) {
        this.loginServer = loginServer;
    }

    @PostMapping("sign/in")
    CommonResult<OpenIdParam> signIn(@RequestBody SignInParam param) {
        return loginServer.signIn(param);
    }

    @GetMapping("sign/off")
    CommonResult<String> signOff(@RequestParam(value = "openid") String openid) {
        return loginServer.signOff(openid);
    }

    @GetMapping("check/role")
    CommonResult<LevelParam> checkRole(@RequestParam(value = "openid") String openid) {
        return loginServer.checkRole(openid);
    }
}
