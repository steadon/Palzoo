package com.sipc.controlserver.service.feign;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.teamServer.PostTeamParam;
import com.sipc.controlserver.pojo.param.teamServer.PostVoteParam;
import com.sipc.controlserver.pojo.result.teamServer.GetTeamIdResult;
import com.sipc.controlserver.pojo.result.teamServer.GetTeamInfoResult;
import com.sipc.controlserver.pojo.result.teamServer.PostTeamResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "team-server")
public interface TeamServer {
    @PostMapping("team-server/team/postVote")
    CommonResult<String> postVote(PostVoteParam param);
    @PostMapping("/team-server/team/postTeam")
    CommonResult<PostTeamResult> postTeam(PostTeamParam param);
    @GetMapping("/team-server/team/getTeamInfo")
    CommonResult<GetTeamInfoResult> getTeamInfo(Integer id);
    @GetMapping("/team-server/team/getTeamId")
    CommonResult<GetTeamIdResult> getTeamId(Integer id);
}
