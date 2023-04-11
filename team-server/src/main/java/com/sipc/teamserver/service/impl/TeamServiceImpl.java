package com.sipc.teamserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sipc.teamserver.constant.Constant;
import com.sipc.teamserver.mapper.TeamMapper;
import com.sipc.teamserver.mapper.VoteMapper;
import com.sipc.teamserver.pojo.CommonResult;
import com.sipc.teamserver.pojo.domain.Team;
import com.sipc.teamserver.pojo.domain.Vote;
import com.sipc.teamserver.pojo.param.PostTeamParam;
import com.sipc.teamserver.pojo.param.PostVoteParam;
import com.sipc.teamserver.pojo.param.topicServer.FinishParam;
import com.sipc.teamserver.pojo.po.VoteInfo;
import com.sipc.teamserver.pojo.result.GetTeamIdResult;
import com.sipc.teamserver.pojo.result.GetTeamInfoResult;
import com.sipc.teamserver.pojo.result.PostTeamResult;
import com.sipc.teamserver.pojo.result.topicServer.DetailNumResult;
import com.sipc.teamserver.pojo.result.topicServer.IsAuthorResult;
import com.sipc.teamserver.pojo.result.userServer.GetUserInfoResult;
import com.sipc.teamserver.service.TeamService;
import com.sipc.teamserver.service.feign.TopicServer;
import com.sipc.teamserver.service.feign.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TeamServiceImpl implements TeamService {
    private final TeamMapper teamMapper;
    private final VoteMapper voteMapper;
    private final TopicServer topicServer;
    private final UserService userService;

    @Autowired
    public TeamServiceImpl(TeamMapper teamMapper, VoteMapper voteMapper, TopicServer topicServer, UserService userService) {
        this.teamMapper = teamMapper;
        this.voteMapper = voteMapper;
        this.topicServer = topicServer;
        this.userService = userService;
    }

    /**
     * @param id 队伍ID
     * @return 队伍信息
     */
    @Override
    public CommonResult<GetTeamInfoResult> getTeamInfo(Integer id) {
        Team team = teamMapper.selectById(id);
        if (team == null)
            return CommonResult.fail("组队不存在");
        GetTeamInfoResult result = new GetTeamInfoResult();
        result.setTeamId(id);
        var detailNum = topicServer.detailNum(team.getPostId());

        if (!Objects.equals(detailNum.getCode(), "00000")){
            detailNum = topicServer.detailNum(id);
        }
        if (!Objects.equals(detailNum.getCode(), "00000"))
            return CommonResult.fail("Topic Server Error: " + detailNum.getMessage());
        result.setExpNum(detailNum.getData().getNum());
        List<VoteInfo> votes = new ArrayList<>();
        List<Vote> teamVotes = voteMapper.selectList(new QueryWrapper<Vote>().eq("team_id", id));
        int agreenum = 0;
        for (Vote vote : teamVotes){
            VoteInfo vi = new VoteInfo();
            vi.setVote(vote.getVote());
            vi.setUserId(vote.getUserId());
            CommonResult<GetUserInfoResult> userInfo = userService.getUserInfo(vote.getUserId());
            if (!Objects.equals(userInfo.getCode(), "00000")){
                userInfo = userService.getUserInfo(vote.getUserId());
            }
            if (!Objects.equals(userInfo.getCode(), "00000"))
                return CommonResult.fail("User Server Error: " + userInfo.getMessage());
            vi.setUserName(userInfo.getData().getUsername());
            if (vote.getVote())
                agreenum++;
            votes.add(vi);
        }
        result.setVotes(votes);
        result.setAgreeNum(agreenum);
        result.setActNum(teamVotes.size());
        return CommonResult.success(result);
    }

    /**
     * @param param 用户 ID 、队伍 ID 与投票信息
     * @return 处理结果
     */
    @Override
    public CommonResult<String> postVote(PostVoteParam param) {
        Long count = voteMapper.selectCount(
                new QueryWrapper<Vote>()
                        .eq("user_id", param.getUserId())
                        .eq("team_id", param.getTeamId())
        );
        if (count != 0){
            return CommonResult.fail("请勿重复投票");
        }
        Long voteCount = voteMapper.selectCount(
                new QueryWrapper<Vote>()
                        .eq("team_id", param.getTeamId())
        );
        var detailNum = topicServer.detailNum(param.getTeamId());
        if (!Objects.equals(detailNum.getCode(), "00000")){
            detailNum = topicServer.detailNum(param.getTeamId());
        }
        if (!Objects.equals(detailNum.getCode(), "00000"))
            return CommonResult.fail("Topic Server Error: " + detailNum.getMessage());
        if (voteCount >= detailNum.getData().getNum())
            return CommonResult.fail("投票人数已达上限");
        Vote vote = new Vote();
        vote.setUserId(param.getUserId());
        vote.setTeamId(param.getTeamId());
        vote.setCreateTime(LocalDateTime.now());
        vote.setUpdateTime(LocalDateTime.now());
        int i = voteMapper.insert(vote);
        if (i == 1) {
            if (voteCount == detailNum.getData().getNum() - 1) {
                Team team = teamMapper.selectById(param.getTeamId());
                var finishParam = new FinishParam();
                finishParam.setPostId(team.getPostId());
                CommonResult<String> finish = topicServer.finish(finishParam);
                if (!Objects.equals(finish.getCode(), "00000"))
                    finish = topicServer.finish(finishParam);
                if (!Objects.equals(finish.getCode(), "00000"))
                    log.warn("Post Vote Finish Error: " +
                            "PostID = " + finishParam.getPostId() + ", " +
                            "TeamID = " + param.getTeamId() + ", " +
                            "Error: " + finish.getMessage());

            }
            return CommonResult.success("请求正常");
        }
        else
            return CommonResult.fail("数据库错误");
    }

    /**
     * @param param 帖子信息与投票结束时间
     * @return 队伍信息
     */
    @Override
    public CommonResult<PostTeamResult> postTeam(PostTeamParam param) {
        Long count = teamMapper.selectCount(new QueryWrapper<Team>()
                .eq("post_id", param.getPostId()));
        if (count != 0)
            return CommonResult.fail("投票已存在，请勿重复发起投票");
        CommonResult<IsAuthorResult> isAuthor = topicServer.isAuthor(param.getUserId(), param.getPostId());
        if (!Objects.equals(isAuthor.getCode(), "00000"))
            isAuthor = topicServer.isAuthor(param.getUserId(), param.getPostId());
        if (!Objects.equals(isAuthor.getCode(), "00000"))
            return CommonResult.fail("Check Author From Topic Server Error: " + isAuthor.getMessage());
        if (!isAuthor.getData().getIsAuthor()) {
            return CommonResult.fail("您不是帖子作者，无权开启投票");
        }
        var team = new Team();
        team.setPostId(param.getPostId());
        team.setEndTime(LocalDateTime.parse(param.getEndTime(), Constant.dateTimeFormatter));
        team.setStartTime(LocalDateTime.now());
        if (team.getStartTime().isAfter(team.getEndTime()))
            return CommonResult.fail("结束时间早于现在，创建投票失败");
        team.setCreateTime(LocalDateTime.now());
        team.setUpdateTime(team.getCreateTime());
        int i = teamMapper.insert(team);
        if (i != 1)
            return CommonResult.fail("数据库错误");
        var result = new PostTeamResult();
        result.setTeamId(team.getId());
        return CommonResult.success(result);
    }

    /**
     * @param id Post ID
     * @return Team Id
     */
    @Override
    public CommonResult<GetTeamIdResult> getTeamId(Integer id) {
        List<Team> teams = teamMapper.selectList(new QueryWrapper<Team>()
                .eq("post_id", id));
        if (teams.size() == 0)
            return CommonResult.fail("组队不存在");
        if (teams.size() > 1)
            return CommonResult.fail("数据库错误");
        var result = new GetTeamIdResult();
        result.setTeamId(teams.get(0).getId());
        return CommonResult.success(result);
    }
}
