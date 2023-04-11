package com.sipc.teamserver.pojo.param;

import lombok.Data;

/**
 * 提交投票的请求体
 * @author DoudiNCer
 */
@Data
public class PostVoteParam {
    // 提交者用户 ID
    private Integer userId;
    // 队伍（投票）ID
    private Integer teamId;
    // 投票意愿（是否同意）
    private Boolean vote;
}
