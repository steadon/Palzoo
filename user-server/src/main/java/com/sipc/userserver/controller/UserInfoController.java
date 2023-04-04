package com.sipc.userserver.controller;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.param.DropUserInfoParam;
import com.sipc.userserver.pojo.param.PostNewUserIdParam;
import com.sipc.userserver.pojo.result.GetUserInfoResult;
import com.sipc.userserver.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
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
    public CommonResult<Null> postNewUserInfo(@RequestBody PostNewUserIdParam param){
        return userInfoService.postNewUserInfo(param);
    }

    @PostMapping("/info/drop")
    public CommonResult<Null> dropUserInfo(@RequestBody DropUserInfoParam param){
        return userInfoService.dropUserInfo(param);
    }
}
