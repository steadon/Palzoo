package com.sipc.loginserver.controller;

import com.sipc.loginserver.pojo.CommonResult;
import com.sipc.loginserver.pojo.domain.User;
import com.sipc.loginserver.pojo.param.LevelParam;
import com.sipc.loginserver.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 鉴权业务块
 *
 * @author Sterben
 * @version 1.0.0
 */
@RestController
@RequestMapping("/check")
public class CheckController {
    private final CheckService checkService;

    @Autowired
    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    /**
     * 鉴定用户合法性
     *
     * @param openid 微信用户唯一标识
     * @return 用户权限
     */
    @GetMapping("/role")
    public CommonResult<LevelParam> checkRole(@RequestParam String openid) {
        return checkService.checkRole(openid);
    }

    /**
     * 鉴定用户存在性并获取用户主体
     *
     * @param openid 微信用户唯一标识
     * @return 用户主体
     */
    @GetMapping("/user")
    public User getUser(@RequestParam(value = "openid") String openid) {
        return checkService.checkUser(openid);
    }
}
