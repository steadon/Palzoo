package com.sipc.topicserver.pojo.dto.result.teamServer;

import lombok.Data;

import java.util.List;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/11 15:09
 */
@Data
public class GetTeamInfoResult {
    // 队伍ID
    private Integer teamId;
    // 队伍现有人数
    private Integer actNum;
    // 队伍最大人数
    private Integer expNum;
    // 同意人数
    private Integer agreeNum;
    // 投票情况
    private List<VoteInfo> votes;
}
