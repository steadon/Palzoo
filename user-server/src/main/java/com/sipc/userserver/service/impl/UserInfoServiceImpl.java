package com.sipc.userserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sipc.userserver.mapper.AcaMajorMapper;
import com.sipc.userserver.mapper.UserInfoMapper;
import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.domain.AcaMajor;
import com.sipc.userserver.pojo.domain.UserInfo;
import com.sipc.userserver.pojo.param.DropUserInfoParam;
import com.sipc.userserver.pojo.param.PostNewUserIdParam;
import com.sipc.userserver.pojo.param.UpdateUserInfoParam;
import com.sipc.userserver.pojo.result.GetUserInfoResult;
import com.sipc.userserver.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoMapper userInfoMapper;
    private final AcaMajorMapper acaMajorMapper;

    @Autowired
    public UserInfoServiceImpl(UserInfoMapper userInfoMapper, AcaMajorMapper acaMajorMapper) {
        this.userInfoMapper = userInfoMapper;
        this.acaMajorMapper = acaMajorMapper;
    }

    /**
     * 获取用户信息
     *
     * @param uid 用户ID
     * @return 用户信息
     * @author DoudiNCer
     */
    @Override
    public CommonResult<GetUserInfoResult> getUserInfo(Integer uid) {
        UserInfo userInfo = userInfoMapper.selectById(uid);
        if (userInfo == null)
            return CommonResult.fail("查无此人");

        GetUserInfoResult result = new GetUserInfoResult();
        result.setUserId(uid);
        result.setUsername(userInfo.getUserName());
        if (userInfo.getAcaMajorId() != null) {
            AcaMajor acaMajor = acaMajorMapper.selectById(userInfo.getAcaMajorId());
            if (acaMajor == null)
                return CommonResult.fail("数据错误，查无专业信息");
            result.setAcademy(acaMajor.getAcaName());
            result.setMajor(acaMajor.getMajorName());
        }
        if (userInfo.getPhone() != null)
            result.setPhone(userInfo.getPhone());
        if (userInfo.getGender() != null)
            result.setGender(userInfo.getGender() ? "男" : "女");
        if (userInfo.getAvatarUrl() != null)
            result.setAvatarUrl(userInfo.getAvatarUrl());
        return CommonResult.success(result);
    }

    /**
     * 创建新用户信息
     * @param param 新用户的 userID 与 openID
     * @return 处理结果
     * @author DoudiNCer
     */
    @Override
    public CommonResult<String> postNewUserInfo(PostNewUserIdParam param) {
        UserInfo ifexist = userInfoMapper.selectById(param.getUserId());
        if (ifexist != null)
            return CommonResult.fail("用户ID已存在");

        UserInfo ui = new UserInfo();
        ui.setUserId(param.getUserId());
        ui.setCreateTime(LocalDateTime.now());
        ui.setUpdateTime(LocalDateTime.now());
        ui.setIsDeleted((byte) 0);
        int insert = userInfoMapper.insert(ui);
        if (insert != 1)
            return CommonResult.fail("服务器错误");
        return CommonResult.success("请求正常");
    }

    /**
     * 删除用户信息
     *
     * @param param 用户ID
     * @return 处理结果
     * @author DoudiNCer
     */
    @Override
    public CommonResult<String> dropUserInfo(DropUserInfoParam param) {
        int i = userInfoMapper.deleteById(param.getUserId());
        if (i != 1)
            return CommonResult.fail("系统错误");
        return CommonResult.success("请求正常");
    }

    /**
     * 更新用户信息
     *
     * @param param 必须有用户ID（UserID），可选参数有用户名、性别、学院专业ID、手机号、用户头像
     * @return 处理结果
     * @author DoudiNCer
     */
    @Override
    public CommonResult<String> UpdateUserInfo(UpdateUserInfoParam param) {
        UserInfo userInfo = userInfoMapper.selectById(param.getUserId());
        if (userInfo == null)
            return CommonResult.fail("用户不存在");
        userInfo.setUserId(param.getUserId());
        userInfo.setUpdateTime(LocalDateTime.now());
        if (param.getUserName() != null && param.getUserName().length() != 0)
            userInfo.setUserName(param.getUserName());
        if (param.getAcaMajorId() != null && param.getAcaMajorId() != 0)
            userInfo.setAcaMajorId(param.getAcaMajorId());
        if (param.getPhone() != null && param.getPhone().length() != 0)
            userInfo.setPhone(param.getPhone());
        if (param.getGender() != null)
            userInfo.setGender(param.getGender());
        if (param.getAvatarUrl() != null && param.getAvatarUrl().length() != 0)
            userInfo.setAvatarUrl(param.getAvatarUrl());
        userInfoMapper.updateById(userInfo);
        return CommonResult.success("请求正常");
    }
}
