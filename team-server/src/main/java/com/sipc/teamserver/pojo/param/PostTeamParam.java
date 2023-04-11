package com.sipc.teamserver.pojo.param;

import lombok.Data;

/**
 * 创建投票的请求体
 * @author DoudiNCer
 */
@Data
public class PostTeamParam {
    // 创建者的用户 ID
    private Integer userId;
    // 帖子 ID
    private Integer postId;
    // 结束时间
    private String endTime;
}
