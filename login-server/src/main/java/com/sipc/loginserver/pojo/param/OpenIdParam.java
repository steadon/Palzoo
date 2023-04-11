package com.sipc.loginserver.pojo.param;

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
        //该无参构造用于反序列化，不可删除
    }
}
