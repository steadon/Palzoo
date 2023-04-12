package com.sipc.userserver.controller;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.param.DropUserInfoParam;
import com.sipc.userserver.pojo.param.PostNewUserIdParam;
import com.sipc.userserver.pojo.param.UpdateUserAvatarParam;
import com.sipc.userserver.pojo.param.UpdateUserInfoParam;
import com.sipc.userserver.pojo.result.GetUserInfoResult;
import com.sipc.userserver.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserInfoController {
    private final UserInfoService userInfoService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 获取用户信息
     * @param uid 要获取信息的用户的 UserID
     * @return 用户信息
     * @author DoudiNCer
     */
    @GetMapping("/info/get")
    public CommonResult<GetUserInfoResult> getUserInfo(@RequestParam("userId") Integer uid){
        return userInfoService.getUserInfo(uid);
    }

    /**
     * 新建用户信息
     * @param param 用户的 UserID、OpenID
     * @return 处理结果
     * @author DoudiNCer
     */
    @PostMapping("/info/postnew")
    public CommonResult<String> postNewUserInfo(@RequestBody PostNewUserIdParam param){
        return userInfoService.postNewUserInfo(param);
    }

    /**
     * 删除用户信息
     * @param param 用户的 UserID
     * @return 处理结果
     * @author DoudiNCer
     */
    @PostMapping("/info/drop")
    public CommonResult<String> dropUserInfo(@RequestBody DropUserInfoParam param){
        return userInfoService.dropUserInfo(param);
    }

    /**
     * 更新用户信息
     * @param param 必填用户的 UserID,选填用户昵称、性别、手机号、学院专业代码
     * @return 处理结果
     * @author DoudiNCer
     */
    @PostMapping("/info/update")
    public CommonResult<String> updateUserInfo(@RequestBody UpdateUserInfoParam param){
        return userInfoService.UpdateUserInfo(param);
    }

    @PostMapping("/info/updateAvatar")
    public CommonResult<String> updateUserAvatar(@RequestParam("avatar") MultipartFile file, @RequestBody UpdateUserAvatarParam param){
        return userInfoService.UpdateUserAvatar(file, param);
    }
}
