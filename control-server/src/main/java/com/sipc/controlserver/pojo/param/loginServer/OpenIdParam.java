package com.sipc.controlserver.pojo.param.loginServer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpenIdParam {
    private String sessionKey;
    private String openid;
    private String username;
    private String avatarUrl;

    public OpenIdParam() {

    }
}
