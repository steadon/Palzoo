package com.sipc.controlserver.controller;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.User;
import com.sipc.controlserver.pojo.param.userServer.DropUserInfoParam;
import com.sipc.controlserver.pojo.param.userServer.PostNewUserIdParam;
import com.sipc.controlserver.pojo.param.userServer.UpdateUserInfoParam;
import com.sipc.controlserver.pojo.result.userServer.AcaMajorInfo;
import com.sipc.controlserver.pojo.result.userServer.GetUserInfoResult;
import com.sipc.controlserver.service.feign.LoginServer;
import com.sipc.controlserver.service.feign.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("control")
public class UserController {
    private final UserService userService;

    private final LoginServer loginServer;

    @Autowired
    public UserController(UserService userService, LoginServer loginServer) {
        this.userService = userService;
        this.loginServer = loginServer;
    }

    @GetMapping("user/info/get")
    public CommonResult<GetUserInfoResult> getUserInfo(@RequestParam("openid") String openid) {
        //鉴权
        try {
            loginServer.checkRole(openid);
        } catch (RuntimeException e) {
            log.info("check role failed: " + e);
        }
        //openid 获取 uid
        User user = loginServer.getUser(openid);
        return userService.getUserInfo(user.getId());
    }

    @PostMapping("user/info/postnew")
    public CommonResult<String> postNewUserInfo(@RequestBody PostNewUserIdParam param) {
        return userService.postNewUserInfo(param);
    }

    @PostMapping("user/info/drop")
    public CommonResult<String> dropUserInfo(@RequestBody DropUserInfoParam param) {
        return userService.dropUserInfo(param);
    }

    @PostMapping("user/info/update")
    public CommonResult<String> updateUserInfo(@RequestBody UpdateUserInfoParam param) {
        return userService.UpdateUserInfo(param);
    }

    @GetMapping("user/acamajor/get")
    public CommonResult<List<AcaMajorInfo>> getAllAcaMajorInfo() {
        return userService.getAllAcamajorInfo();
    }
}
