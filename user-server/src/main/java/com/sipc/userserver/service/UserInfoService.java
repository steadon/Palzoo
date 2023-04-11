package com.sipc.userserver.service;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.param.DropUserInfoParam;
import com.sipc.userserver.pojo.param.PostNewUserIdParam;
import com.sipc.userserver.pojo.param.UpdateUserInfoParam;
import com.sipc.userserver.pojo.result.GetUserInfoResult;

public interface UserInfoService {

    /**
     * 获取用户信息
     * @param uid 用户ID
     * @return 用户信息
     * @author DoudiNCer
     */
    CommonResult<GetUserInfoResult> getUserInfo(Integer uid);

    /**
     * 获取用户信息
     * @param param 新用户的 userID 与 openID
     * @return 处理结果，包括用户的UserID、UserName，可能不存在的用户学院、专业、性别、手机号、用户头像
     * @author DoudiNCer
     */
    CommonResult<String> postNewUserInfo(PostNewUserIdParam param);

    /**
     * 删除用户信息
     * @param param 用户ID
     * @return 处理结果
     * @author DoudiNCer
     */
    CommonResult<String> dropUserInfo(DropUserInfoParam param);

    /**
     * 更新用户信息
     * @param param 必须有用户ID（UserID），可选参数有用户名、性别、学院专业ID、手机号、用户头像
     * @return 处理结果
     * @author DoudiNCer
     */
    CommonResult<String> UpdateUserInfo(UpdateUserInfoParam param);
}
