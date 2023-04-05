package com.sipc.controlserver.service.feign;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.userServer.DropUserInfoParam;
import com.sipc.controlserver.pojo.param.userServer.PostNewUserIdParam;
import com.sipc.controlserver.pojo.param.userServer.UpdateUserInfoParam;
import com.sipc.controlserver.pojo.result.userServer.AcaMajorInfo;
import com.sipc.controlserver.pojo.result.userServer.GetUserInfoResult;
import org.apache.ibatis.jdbc.Null;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "user-server")
public interface UserService {
    @GetMapping("/user-server/user/info/get")
    CommonResult<GetUserInfoResult> getUserInfo(Integer uid);
    @PostMapping("/user-server/user/info/postnew")
    CommonResult<Null> postNewUserInfo(PostNewUserIdParam param);
    @PostMapping("/user-server/user/info/drop")
    CommonResult<Null> dropUserInfo(DropUserInfoParam param);
    @PostMapping("/user-server/user/info/update")
    CommonResult<Null> UpdateUserInfo(UpdateUserInfoParam param);
    @GetMapping("/user-server/acamajor/get")
    CommonResult<List<AcaMajorInfo>> getAllAcamajorInfo();
}
