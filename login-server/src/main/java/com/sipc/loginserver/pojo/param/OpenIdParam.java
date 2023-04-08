package com.sipc.loginserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpenIdParam {
    private String sessionKey;
    private String openid;

    public OpenIdParam() {

    }
}
