package com.sipc.controlserver.pojo.param.userServer;

import lombok.Data;

/**
 * 删除用户信息的请求体
 * @author DoudiNCer
 */
@Data
public class DropUserInfoParam {
    // 用户ID（UserID）
    private Integer userId;
}
