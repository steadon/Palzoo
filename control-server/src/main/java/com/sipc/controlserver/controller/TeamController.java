package com.sipc.controlserver.controller;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.User;
import com.sipc.controlserver.pojo.param.teamServer.PostTeamParam;
import com.sipc.controlserver.pojo.param.teamServer.PostVoteParam;
import com.sipc.controlserver.pojo.result.teamServer.GetTeamIdResult;
import com.sipc.controlserver.pojo.result.teamServer.GetTeamInfoResult;
import com.sipc.controlserver.pojo.result.teamServer.PostTeamResult;
import com.sipc.controlserver.service.feign.LoginServer;
import com.sipc.controlserver.service.feign.TeamServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("control")
@Slf4j
public class TeamController {
    private final TeamServer teamServer;

    private final LoginServer loginServer;

    @Autowired
    public TeamController(TeamServer teamServer, LoginServer loginServer) {
        this.teamServer = teamServer;
        this.loginServer = loginServer;
    }

    @PostMapping("team/postVote")
    public CommonResult<String> postVote(@RequestBody PostVoteParam param){
        // 鉴权
        try {
            loginServer.checkRole(param.getOpenId());
        } catch (RuntimeException e) {
            log.info("check role failed: " + e);
        }
        // openid 获取 uid
        User user = loginServer.getUser(param.getOpenId());
        param.setUserId(user.getId());
        return teamServer.postVote(param);
    }

    @PostMapping("team/postTeam")
    public CommonResult<PostTeamResult> postTeam(@RequestBody PostTeamParam param){
        // 鉴权
        try {
            loginServer.checkRole(param.getOpenId());
        } catch (RuntimeException e) {
            log.info("check role failed: " + e);
        }
        // openid 获取 uid
        User user = loginServer.getUser(param.getOpenId());
        param.setUserId(user.getId());
        return teamServer.postTeam(param);
    }

    @GetMapping("team/getTeamInfo")
    public CommonResult<GetTeamInfoResult> getTeamInfo(@RequestParam("id") Integer id, @RequestParam("openId") String openId){
        // 鉴权
        try {
            loginServer.checkRole(openId);
        } catch (RuntimeException e) {
            log.info("check role failed: " + e);
        }
        // openid 获取 uid
        User user = loginServer.getUser(openId);
        return teamServer.getTeamInfo(id);
    }

    @GetMapping("team/getTeamId")
    public CommonResult<GetTeamIdResult> getTeamId(@RequestParam("postId") Integer id, @RequestParam("openId") String openId){
        // 鉴权
        try {
            loginServer.checkRole(openId);
        } catch (RuntimeException e) {
            log.info("check role failed: " + e);
        }
        // openid 获取 uid
        User user = loginServer.getUser(openId);
        return teamServer.getTeamId(id);
    }

}
