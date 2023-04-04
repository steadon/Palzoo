package com.sipc.loginserver.service;

import com.sipc.loginserver.pojo.CommonResult;
import com.sipc.loginserver.pojo.param.OpenIdParam;
import com.sipc.loginserver.pojo.param.SignInParam;

public interface LoginService {
    CommonResult<OpenIdParam> signIn(SignInParam param);
}
