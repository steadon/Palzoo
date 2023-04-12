package com.sipc.controlserver.controller;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.User;
import com.sipc.controlserver.pojo.param.userServer.UpdateUserAvatarParam;
import com.sipc.controlserver.pojo.param.userServer.UpdateUserInfoParam;
import com.sipc.controlserver.pojo.result.userServer.AcaMajorInfo;
import com.sipc.controlserver.pojo.result.userServer.GetUserInfoResult;
import com.sipc.controlserver.service.feign.LoginServer;
import com.sipc.controlserver.service.feign.UserServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("control")
public class UserController {
    private final UserServer userServer;

    private final LoginServer loginServer;

    @Autowired
    public UserController(UserServer userServer, LoginServer loginServer) {
        this.userServer = userServer;
        this.loginServer = loginServer;
    }

    @GetMapping("user/info/get")
    public CommonResult<GetUserInfoResult> getUserInfo(@RequestParam("openId") String openid, @RequestParam("userId") Integer userId) {
        //鉴权
        try {
            loginServer.checkRole(openid);
        } catch (RuntimeException e) {
            log.info("check role failed: " + e);
        }
        //openid 获取 uid
        User user = loginServer.getUser(openid);
        if (userId == 0)
            userId = user.getId();
        return userServer.getUserInfo(userId);
    }

    @PostMapping("user/info/update")
    public CommonResult<String> updateUserInfo(@RequestBody UpdateUserInfoParam param) {
        // 鉴权
        try {
            loginServer.checkRole(param.getOpenId());
        } catch (RuntimeException e) {
            log.info("check role failed: " + e);
        }
        //openid 获取 uid
        User user = loginServer.getUser(param.getOpenId());
        param.setUserId(user.getId());
        return userServer.UpdateUserInfo(param);
    }

    @GetMapping("user/acamajor/get")
    public CommonResult<List<AcaMajorInfo>> getAllAcaMajorInfo() {
        return userServer.getAllAcamajorInfo();
    }

    @PostMapping("user/info/updateAvatar")
    public CommonResult<String> updateUserAvatar(@RequestParam("avatar") MultipartFile file, @RequestBody UpdateUserAvatarParam param){
        // 鉴权
        try {
            loginServer.checkRole(param.getOpenId());
        } catch (RuntimeException e) {
            log.info("check role failed: " + e);
        }
        //openid 获取 uid
        User user = loginServer.getUser(param.getOpenId());
        param.setUserId(user.getId());
        return userServer.updateUserAvatar(file, param);
    }
}
