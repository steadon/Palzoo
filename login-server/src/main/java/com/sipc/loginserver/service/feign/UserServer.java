package com.sipc.loginserver.service.feign;

import com.sipc.loginserver.pojo.CommonResult;
import com.sipc.loginserver.pojo.param.DropUserInfoParam;
import com.sipc.loginserver.pojo.param.GetUserInfoParam;
import com.sipc.loginserver.pojo.param.PostNewUserIdParam;
import org.apache.ibatis.jdbc.Null;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用用户模块
 */
@FeignClient(value = "user-server")
public interface UserServer {
    @PostMapping("/user/info/postnew")
    CommonResult<Null> postNewUserInfo(@RequestBody PostNewUserIdParam param);

    @PostMapping("/user/info/drop")
    CommonResult<String> dropUserInfo(@RequestBody DropUserInfoParam param);

    @GetMapping("/user/info/get")
    CommonResult<GetUserInfoParam> getUserInfo(@RequestParam("userId") Integer uid);
}
