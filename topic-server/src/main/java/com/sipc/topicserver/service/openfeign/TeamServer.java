package com.sipc.topicserver.service.openfeign;

import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.result.teamServer.GetTeamIdResult;
import com.sipc.topicserver.pojo.dto.result.teamServer.GetTeamInfoResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("team-server")
public interface TeamServer {

    @GetMapping("/team/getTeamInfo")
    CommonResult<GetTeamInfoResult> getTeamInfo(@RequestParam("id") Integer id);

    @GetMapping("/team/getTeamId")
    CommonResult<GetTeamIdResult> getTeamId(@RequestParam("postId") Integer id);
}
