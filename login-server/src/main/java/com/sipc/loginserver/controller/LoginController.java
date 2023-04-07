package com.sipc.loginserver.controller;

import com.sipc.loginserver.pojo.CommonResult;
import com.sipc.loginserver.pojo.param.OpenIdParam;
import com.sipc.loginserver.pojo.param.SignInParam;
import com.sipc.loginserver.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/in")
    public CommonResult<OpenIdParam> signIn(@RequestBody SignInParam param) {
        return loginService.signIn(param);
    }

    @GetMapping("/off")
    CommonResult<String> signOff(@RequestParam String openid) {
        return loginService.signOff(openid);
    }
}
