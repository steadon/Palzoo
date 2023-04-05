package com.sipc.loginserver.controller;

import com.sipc.loginserver.pojo.CommonResult;
import com.sipc.loginserver.pojo.domain.User;
import com.sipc.loginserver.pojo.param.LevelParam;
import com.sipc.loginserver.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckController {
    private final CheckService checkService;

    @Autowired
    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @GetMapping("check/role")
    public CommonResult<LevelParam> checkRole(@RequestParam String openid) {
        return checkService.checkRole(openid);
    }

    @GetMapping("check/user")
    public User getUser(@RequestParam(value = "openid") String openid) {
        return checkService.getUser(openid);
    }
}
