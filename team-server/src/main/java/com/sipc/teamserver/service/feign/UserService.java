package com.sipc.teamserver.service.feign;

import com.sipc.teamserver.pojo.CommonResult;
import com.sipc.teamserver.pojo.result.userServer.GetUserInfoResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "user-server")
public interface UserService {
    @GetMapping("/user/info/get")
    CommonResult<GetUserInfoResult> getUserInfo(@RequestParam("userId") Integer uid);
}
