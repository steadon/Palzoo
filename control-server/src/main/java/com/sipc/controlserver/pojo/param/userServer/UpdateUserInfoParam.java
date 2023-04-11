package com.sipc.controlserver.pojo.param.userServer;

import lombok.Data;

/**
 * 更新用户信息的请求体
 * @author DoudiNCer
 */
@Data
public class UpdateUserInfoParam {
    // OpenID
    private String openId;
    // 用户的 UserID
    private Integer userId;
    // 用户昵称
    private String userName;
    // 学院专业 ID
    private Integer acaMajorId;
    // 性别，True 男，False女
    private Boolean gender;
    // 手机号
    private String phone;
    // 用户头像
    private String avatarUrl;
}
