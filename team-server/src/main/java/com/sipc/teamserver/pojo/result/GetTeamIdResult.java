package com.sipc.teamserver.pojo.result;

import lombok.Data;

/**
 * 根据帖子ID查询投票 ID 的响应体
 * @author DoudiNCer
 */
@Data
public class GetTeamIdResult {
    // 投票ID
    private Integer teamId;
}
