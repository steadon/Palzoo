package com.sipc.userserver.service;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.result.GetUserInfoResult;

public interface UserInfoService {

    CommonResult<GetUserInfoResult> getUserInfo(Integer uid);
}
