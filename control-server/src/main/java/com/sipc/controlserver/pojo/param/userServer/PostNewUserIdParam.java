package com.sipc.controlserver.pojo.param.userServer;

import lombok.Data;

/**
 * 添加新用户信息去请求体
 * @author DoudiNCer
 */
@Data
public class PostNewUserIdParam {
    // 用户的 UserID
    private Integer userId;
    // 微信提供的用户的 OpenID
    private String openId;
}
