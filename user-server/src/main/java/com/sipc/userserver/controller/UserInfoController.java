package com.sipc.userserver.controller;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.param.DropUserInfoParam;
import com.sipc.userserver.pojo.param.PostNewUserIdParam;
import com.sipc.userserver.pojo.param.UpdateUserInfoParam;
import com.sipc.userserver.pojo.result.GetUserInfoResult;
import com.sipc.userserver.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController {
    private final UserInfoService userInfoService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/info/get")
    public CommonResult<GetUserInfoResult> getUserInfo(@RequestParam("userId") Integer uid){
        return userInfoService.getUserInfo(uid);
    }

    @PostMapping("/info/postnew")
    public CommonResult<String> postNewUserInfo(@RequestBody PostNewUserIdParam param){
        return userInfoService.postNewUserInfo(param);
    }

    @PostMapping("/info/drop")
    public CommonResult<String> dropUserInfo(@RequestBody DropUserInfoParam param){
        return userInfoService.dropUserInfo(param);
    }

    @PostMapping("/info/update")
    public CommonResult<String> updateUserInfo(@RequestBody UpdateUserInfoParam param){
        return userInfoService.UpdateUserInfo(param);
    }
}
