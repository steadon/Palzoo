package com.sipc.controlserver.pojo.result.userServer;

import lombok.Data;

/**
 * 查询用户信息的返回结果
 * @author DoudiNCer
 */
@Data
public class GetUserInfoResult {
    // 用户ID（UserID）
    private Integer userId;
    // 用户名
    private String username;
    // 学院名
    private String academy;
    // 专业名
    private String major;
    // 性别（男/女）
    private String gender;
    // 手机号
    private String phone;
    // 用户头像
    private String avatarUrl;
}
