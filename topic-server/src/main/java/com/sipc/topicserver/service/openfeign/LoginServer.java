package com.sipc.topicserver.service.openfeign;

import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.result.loginServer.LevelParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("login-server")
public interface LoginServer {

    @GetMapping("/check/role")
    CommonResult<LevelParam> checkRole(@RequestParam(value = "openid") String openid);

}
