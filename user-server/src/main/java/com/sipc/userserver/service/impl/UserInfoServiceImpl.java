package com.sipc.userserver.service.impl;

import com.sipc.userserver.mapper.AcaMajorMapper;
import com.sipc.userserver.mapper.UserInfoMapper;
import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.domain.AcaMajor;
import com.sipc.userserver.pojo.domain.UserInfo;
import com.sipc.userserver.pojo.result.GetUserInfoResult;
import com.sipc.userserver.service.UserInfoService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        AcaMajor acaMajor = acaMajorMapper.selectById(userInfo.getAcaMajorId());
        if (acaMajor == null)
            return CommonResult.fail("数据错误，查无专业信息");
        GetUserInfoResult result = new GetUserInfoResult();
        result.setUserId(uid);
        result.setUsername(userInfo.getUserName());
        result.setAcademy(acaMajor.getAcaName());
        result.setMajor(acaMajor.getMajorName());
        return CommonResult.success(result);
    }
}
