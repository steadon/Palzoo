package com.sipc.userserver.service;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.param.DropUserInfoParam;
import com.sipc.userserver.pojo.param.PostNewUserIdParam;
import com.sipc.userserver.pojo.param.UpdateUserAvatarParam;
import com.sipc.userserver.pojo.param.UpdateUserInfoParam;
import com.sipc.userserver.pojo.result.GetUserInfoResult;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {

    /**
     * 获取用户信息
     * @param uid 用户ID
     * @return 用户信息
     * @author DoudiNCer
     */
    CommonResult<GetUserInfoResult> getUserInfo(Integer uid);

    /**
     * 创建新用户信息
     * @param param 新用户的 userID 与 openID
     * @return 处理结果
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

    CommonResult<String> UpdateUserAvatar(MultipartFile file, UpdateUserAvatarParam param);
}
