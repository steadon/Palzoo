package com.sipc.controlserver.service.feign;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.userServer.DropUserInfoParam;
import com.sipc.controlserver.pojo.param.userServer.PostNewUserIdParam;
import com.sipc.controlserver.pojo.param.userServer.UpdateUserInfoParam;
import com.sipc.controlserver.pojo.result.userServer.AcaMajorInfo;
import com.sipc.controlserver.pojo.result.userServer.GetUserInfoResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(value = "user-server")
public interface UserServer {
    @GetMapping("/user/info/get")
    CommonResult<GetUserInfoResult> getUserInfo(@RequestParam("userId") Integer userId);

    @PostMapping("/user/info/postnew")
    CommonResult<String> postNewUserInfo(@RequestBody PostNewUserIdParam param);

    @PostMapping("/user/info/drop")
    CommonResult<String> dropUserInfo(@RequestBody DropUserInfoParam param);

    @PostMapping("/user/info/update")
    CommonResult<String> UpdateUserInfo(@RequestBody UpdateUserInfoParam param);

    @GetMapping("/user/acamajor/get")
    CommonResult<List<AcaMajorInfo>> getAllAcamajorInfo();

    @PostMapping("/user/info/updateAvatar")
    CommonResult<String> updateUserAvatar(@RequestParam("avatar") MultipartFile file, @RequestParam("userId") Integer userId);
}
