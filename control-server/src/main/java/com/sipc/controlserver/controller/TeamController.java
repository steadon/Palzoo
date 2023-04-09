package com.sipc.controlserver.controller;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.teamServer.PostTeamParam;
import com.sipc.controlserver.pojo.param.teamServer.PostVoteParam;
import com.sipc.controlserver.pojo.result.teamServer.GetTeamIdResult;
import com.sipc.controlserver.pojo.result.teamServer.GetTeamInfoResult;
import com.sipc.controlserver.pojo.result.teamServer.PostTeamResult;
import com.sipc.controlserver.service.feign.TeamServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("control")
public class TeamController {
    private final TeamServer teamServer;

    @Autowired
    public TeamController(TeamServer teamServer) {
        this.teamServer = teamServer;
    }

    @PostMapping("team/postVote")
    public CommonResult<String> postVote(@RequestBody PostVoteParam param){
        return teamServer.postVote(param);
    }

    @PostMapping("team/postTeam")
    public CommonResult<PostTeamResult> postTeam(@RequestBody PostTeamParam param){
        return teamServer.postTeam(param);
    }

    @GetMapping("team/getTeamInfo")
    public CommonResult<GetTeamInfoResult> getTeamInfo(@RequestParam("id") Integer id){
        return teamServer.getTeamInfo(id);
    }

    @GetMapping("team/getTeamId")
    public CommonResult<GetTeamIdResult> getTeamId(@RequestParam("postId") Integer id){
        return teamServer.getTeamId(id);
    }

}
