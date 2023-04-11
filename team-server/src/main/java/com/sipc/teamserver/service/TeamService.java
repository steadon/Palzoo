package com.sipc.teamserver.service;

import com.sipc.teamserver.pojo.CommonResult;
import com.sipc.teamserver.pojo.param.PostTeamParam;
import com.sipc.teamserver.pojo.param.PostVoteParam;
import com.sipc.teamserver.pojo.result.GetTeamIdResult;
import com.sipc.teamserver.pojo.result.GetTeamInfoResult;
import com.sipc.teamserver.pojo.result.PostTeamResult;

public interface TeamService {

    /**
     * 获取队伍（投票）信息
     * @param id 队伍ID
     * @return 队伍信息，包括队伍 ID、现有投票人数、计划投票人数、同意人数、投票者的用户ID、用户昵称与投票意愿
     * @author DoudiNCer
     */
    CommonResult<GetTeamInfoResult> getTeamInfo(Integer id);

    /**
     * 提交投票意愿
     * @param param 用户 ID 、队伍 ID 与投票意愿（是否同意）
     * @return 处理结果
     * @author DoudiNCer
     */
    CommonResult<String> postVote(PostVoteParam param);

    /**
     * 创建组队（投票）
     * @param param 创建者用户 ID、帖子 ID 与投票结束时间（格式为"yyyy-MM-dd HH:mm:ss"）
     * @return 队伍ID
     * @author DoudiNCer
     */
    CommonResult<PostTeamResult> postTeam(PostTeamParam param);

    /**
     * 根据帖子 ID 获取队伍（投票）ID
     * @param id 帖子 ID
     * @return 组队 Id
     * @author DoudiNCer
     */
    CommonResult<GetTeamIdResult> getTeamId(Integer id);
}
