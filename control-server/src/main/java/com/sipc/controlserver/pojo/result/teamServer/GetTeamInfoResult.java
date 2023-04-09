package com.sipc.controlserver.pojo.result.teamServer;

import com.sipc.controlserver.pojo.po.teamServer.VoteInfo;
import lombok.Data;

import java.util.List;

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
