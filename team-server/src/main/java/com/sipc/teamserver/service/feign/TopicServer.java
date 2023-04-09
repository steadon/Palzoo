package com.sipc.teamserver.service.feign;

import com.sipc.teamserver.pojo.CommonResult;
import com.sipc.teamserver.pojo.param.topicServer.FinishParam;
import com.sipc.teamserver.pojo.result.topicServer.DetailNumResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "topic-server")
public interface TopicServer {
    @PostMapping("/topic-server/finish")
    CommonResult<String> finish(@RequestBody FinishParam finishParam);
    @GetMapping("/topic-server/detail/num")
    CommonResult<DetailNumResult> detailNum(@RequestParam(value = "postId") Integer postId);
}
