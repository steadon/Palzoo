package com.sipc.loginserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostNewUserIdParam {
    private Integer userId;
    private String openid;

    public PostNewUserIdParam() {
        //该无参构造用于反序列化，不可删除
    }
}
