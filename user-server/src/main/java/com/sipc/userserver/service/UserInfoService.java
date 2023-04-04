package com.sipc.userserver.service;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.param.DropUserInfoParam;
import com.sipc.userserver.pojo.param.PostNewUserIdParam;
import com.sipc.userserver.pojo.param.UpdateUserInfoParam;
import com.sipc.userserver.pojo.result.GetUserInfoResult;
import org.apache.ibatis.jdbc.Null;

public interface UserInfoService {

    CommonResult<GetUserInfoResult> getUserInfo(Integer uid);

    CommonResult<Null> postNewUserInfo(PostNewUserIdParam param);

    CommonResult<Null> dropUserInfo(DropUserInfoParam param);

    CommonResult<Null> UpdateUserInfo(UpdateUserInfoParam param);
}
