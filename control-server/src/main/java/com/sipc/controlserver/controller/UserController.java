package com.sipc.controlserver.controller;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.userServer.DropUserInfoParam;
import com.sipc.controlserver.pojo.param.userServer.PostNewUserIdParam;
import com.sipc.controlserver.pojo.param.userServer.UpdateUserInfoParam;
import com.sipc.controlserver.pojo.result.userServer.AcaMajorInfo;
import com.sipc.controlserver.pojo.result.userServer.GetUserInfoResult;
import com.sipc.controlserver.service.feign.UserService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("control")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/info/get")
    public CommonResult<GetUserInfoResult> getUserInfo(@RequestParam("userId") Integer uid){
        return userService.getUserInfo(uid);
    }

    @PostMapping("user/info/postnew")
    public CommonResult<Null> postNewUserInfo(@RequestBody PostNewUserIdParam param){
        return userService.postNewUserInfo(param);
    }

    @PostMapping("user/info/drop")
    public CommonResult<Null> dropUserInfo(@RequestBody DropUserInfoParam param){
        return userService.dropUserInfo(param);
    }

    @PostMapping("user/info/update")
    public CommonResult<Null> updateUserInfo(@RequestBody UpdateUserInfoParam param){
        return userService.UpdateUserInfo(param);
    }
    @GetMapping("user/acamajor/get")
    public CommonResult<List<AcaMajorInfo>> getAllAcaMajorInfo(){
        return userService.getAllAcamajorInfo();
    }
}
