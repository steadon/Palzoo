package com.sipc.topicserver.service.openfeign;

import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.mic.result.GetUserInfoResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName userServer
 * Description
 * Author o3141
 * Date 2023/4/4 11:56
 * Version 1.0
 */
@FeignClient("user-server")
public interface UserServer {

    @GetMapping("/user/info/get")
    CommonResult<GetUserInfoResult> getUserInfo(@RequestParam("userId") Integer uid);

}
