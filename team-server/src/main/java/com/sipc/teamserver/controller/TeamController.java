package com.sipc.teamserver.controller;

import com.sipc.teamserver.pojo.CommonResult;
import com.sipc.teamserver.pojo.param.PostTeamParam;
import com.sipc.teamserver.pojo.param.PostVoteParam;
import com.sipc.teamserver.pojo.result.GetTeamIdResult;
import com.sipc.teamserver.pojo.result.GetTeamInfoResult;
import com.sipc.teamserver.pojo.result.PostTeamResult;
import com.sipc.teamserver.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * 提交投票
     * @param param 用户的UserID、队伍ID、投票意愿
     * @return 处理结果
     * @author DoudiNCer
     */
    @PostMapping("/postVote")
    public CommonResult<String> postVote(@RequestBody PostVoteParam param){
        return teamService.postVote(param);
    }

    /**
     * 新建投票
     * @param param 用户ID、帖子ID、投票截止时间
     * @return 投票 ID
     * @author DoudiNCer
     */
    @PostMapping("/postTeam")
    public CommonResult<PostTeamResult> postTeam(@RequestBody PostTeamParam param){
        return teamService.postTeam(param);
    }

    /**
     * 获取队伍信息
     * @param id 投票ID
     * @return 投票（队伍）信息
     * @author DoudiNCer
     */
    @GetMapping("/getTeamInfo")
    public CommonResult<GetTeamInfoResult> getTeamInfo(@RequestParam("id") Integer id){
        return teamService.getTeamInfo(id);
    }

    /**
     * 根据帖子ID查询队伍（投票）ID
     * @param id 帖子 ID
     * @return 投票 ID
     * @author DoudiNCer
     */
    @GetMapping("/getTeamId")
    public CommonResult<GetTeamIdResult> getTeamId(@RequestParam("postId") Integer id){
        return teamService.getTeamId(id);
    }

}
