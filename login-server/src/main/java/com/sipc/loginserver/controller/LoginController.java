package com.sipc.loginserver.controller;

import com.sipc.loginserver.pojo.CommonResult;
import com.sipc.loginserver.pojo.param.OpenIdParam;
import com.sipc.loginserver.pojo.param.SignInParam;
import com.sipc.loginserver.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录注册业务块
 *
 * @author Sterben
 * @version 1.0.0
 */
@RestController
@RequestMapping("/sign")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 注册登录
     *
     * @param param code
     * @return openid
     */
    @PostMapping("/in")
    public CommonResult<OpenIdParam> signIn(@RequestBody SignInParam param) {
        return loginService.signIn(param);
    }

    /**
     * 注销账号
     *
     * @param openid openid
     * @return msg
     */
    @GetMapping("/off")
    CommonResult<String> signOff(@RequestParam String openid) {
        return loginService.signOff(openid);
    }
}
