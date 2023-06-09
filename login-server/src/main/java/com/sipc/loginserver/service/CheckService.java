package com.sipc.loginserver.service;

import com.sipc.loginserver.pojo.CommonResult;
import com.sipc.loginserver.pojo.domain.User;
import com.sipc.loginserver.pojo.param.LevelParam;

public interface CheckService {
    CommonResult<LevelParam> checkRole(String openid);

    User checkUser(String openid);
}
