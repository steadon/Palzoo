package com.sipc.chatserver.service.feign;

import com.sipc.chatserver.pojo.CommonResult;
import com.sipc.chatserver.pojo.param.GetUserInfoParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用用户模块
 */
@FeignClient(value = "user-server")
public interface UserServer {
    @GetMapping("/user/info/get")
    CommonResult<GetUserInfoParam> getUserInfo(@RequestParam("userId") Integer uid);
}
