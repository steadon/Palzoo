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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "team-server")
public interface TeamServer {
    @PostMapping("/team/postVote")
    CommonResult<String> postVote(@RequestBody PostVoteParam param);

    @PostMapping("/team/postTeam")
    CommonResult<PostTeamResult> postTeam(@RequestBody PostTeamParam param);

    @GetMapping("/team/getTeamInfo")
    CommonResult<GetTeamInfoResult> getTeamInfo(@RequestParam("id") Integer id);

    @GetMapping("/team/getTeamId")
    CommonResult<GetTeamIdResult> getTeamId(@RequestParam("id") Integer id);
}
