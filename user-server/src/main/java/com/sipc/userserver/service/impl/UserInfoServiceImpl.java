package com.sipc.userserver.service.impl;

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
import org.apache.ibatis.jdbc.Null;
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
     * @param uid 用户ID
     * @return 用户信息
     */
    @Override
    public CommonResult<GetUserInfoResult> getUserInfo(Integer uid) {
        UserInfo userInfo = userInfoMapper.selectById(uid);
        if (userInfo == null)
            return CommonResult.fail("查无此人");

        GetUserInfoResult result = new GetUserInfoResult();
        result.setUserId(uid);
        result.setUsername(userInfo.getUserName());
        if (userInfo.getAcaMajorId() != null){
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
        return CommonResult.success(result);
    }

    /**
     * @param param 新用户的信息
     * @return 处理结果
     */
    @Override
    public CommonResult<Null> postNewUserInfo(PostNewUserIdParam param) {
        UserInfo ifexist = userInfoMapper.selectById(param.getUserId());
        if (ifexist != null)
            return CommonResult.fail("用户隐已存在");
        UserInfo ui = new UserInfo();
        ui.setUserId(param.getUserId());
        ui.setUserName(param.getUserName());
        ui.setCreateTime(LocalDateTime.now());
        ui.setUpdateTime(LocalDateTime.now());
        ui.setIsDeleted((byte) 0);
        int insert = userInfoMapper.insert(ui);
        if (insert != 1)
            return CommonResult.fail("服务器错误");
        return CommonResult.success("请求正常");
    }

    /**
     * @param param 用户ID
     * @return 处理结果
     */
    @Override
    public CommonResult<Null> dropUserInfo(DropUserInfoParam param) {
        int i = userInfoMapper.deleteById(param.getUserId());
        if (i != 1)
            return CommonResult.fail("系统错误");
        return CommonResult.success("请求正常");
    }

    /**
     * @param param 用户ID与要修改的信息
     * @return 处理结果
     */
    @Override
    public CommonResult<Null> UpdateUserInfo(UpdateUserInfoParam param) {
        UserInfo old = userInfoMapper.selectById(param.getUserId());
        if (old == null)
            return CommonResult.fail("用户不存在");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(param.getUserId());
        userInfo.setUpdateTime(LocalDateTime.now());
        if (param.getUserName() != null)
            userInfo.setUserName(param.getUserName());
        if (param.getAcaMajorId() != null)
            userInfo.setAcaMajorId(param.getAcaMajorId());
        if (param.getPhone() != null)
            userInfo.setPhone(param.getPhone());
        if (param.getGender() != null)
            userInfo.setGender(param.getGender());
        userInfoMapper.updateById(userInfo);
        return CommonResult.success("请求正常");
    }
}
