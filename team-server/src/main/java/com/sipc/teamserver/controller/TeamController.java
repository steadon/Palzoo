package com.sipc.teamserver.controller;

import com.sipc.teamserver.pojo.CommonResult;
import com.sipc.teamserver.pojo.param.PostTeamParam;
import com.sipc.teamserver.pojo.param.PostVoteParam;
import com.sipc.teamserver.pojo.result.GetTeamIdResult;
import com.sipc.teamserver.pojo.result.GetTeamInfoResult;
import com.sipc.teamserver.pojo.result.PostTeamResult;
import com.sipc.teamserver.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/postVote")
    public CommonResult<String> postVote(@RequestBody PostVoteParam param){
        return teamService.postVote(param);
    }

    @PostMapping("/postTeam")
    public CommonResult<PostTeamResult> postTeam(@RequestBody PostTeamParam param){
        return teamService.postTeam(param);
    }

    @GetMapping("/getTeamInfo")
    public CommonResult<GetTeamInfoResult> getTeamInfo(@RequestParam("id") Integer id){
        return teamService.getTeamInfo(id);
    }

    @GetMapping("/getTeamId")
    public CommonResult<GetTeamIdResult> getTeamId(@RequestParam("postId") Integer id){
        return teamService.getTeamId(id);
    }

}
