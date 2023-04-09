package com.sipc.teamserver.service;

import com.sipc.teamserver.pojo.CommonResult;
import com.sipc.teamserver.pojo.param.PostTeamParam;
import com.sipc.teamserver.pojo.param.PostVoteParam;
import com.sipc.teamserver.pojo.result.GetTeamIdResult;
import com.sipc.teamserver.pojo.result.GetTeamInfoResult;
import com.sipc.teamserver.pojo.result.PostTeamResult;

public interface TeamService {
    CommonResult<GetTeamInfoResult> getTeamInfo(Integer id);

    CommonResult<String> postVote(PostVoteParam param);

    CommonResult<PostTeamResult> postTeam(PostTeamParam param);

    CommonResult<GetTeamIdResult> getTeamId(Integer id);
}
