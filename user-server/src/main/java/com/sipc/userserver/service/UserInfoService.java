package com.sipc.userserver.service;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.param.DropUserInfoParam;
import com.sipc.userserver.pojo.param.PostNewUserIdParam;
import com.sipc.userserver.pojo.param.UpdateUserInfoParam;
import com.sipc.userserver.pojo.result.GetUserInfoResult;

public interface UserInfoService {

    CommonResult<GetUserInfoResult> getUserInfo(Integer uid);

    CommonResult<String> postNewUserInfo(PostNewUserIdParam param);

    CommonResult<String> dropUserInfo(DropUserInfoParam param);

    CommonResult<String> UpdateUserInfo(UpdateUserInfoParam param);
}
