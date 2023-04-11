package com.sipc.teamserver.pojo.po;

import lombok.Data;

/**
 * 投票信息
 * @author DoudiNCer
 */
@Data
public class VoteInfo {
    // 投票者用户ID
    private Integer userId;
    // 投票意愿（是否同意）
    private Boolean vote;
    // 投票者昵称
    private String userName;
}
