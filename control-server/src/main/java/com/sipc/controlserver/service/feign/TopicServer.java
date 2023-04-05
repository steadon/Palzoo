package com.sipc.controlserver.service.feign;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.topicServer.DeleteParam;
import com.sipc.controlserver.pojo.param.topicServer.FinishParam;
import com.sipc.controlserver.pojo.param.topicServer.SearchParam;
import com.sipc.controlserver.pojo.param.topicServer.SubmitParam;
import com.sipc.controlserver.pojo.result.topicServer.DetailResult;
import com.sipc.controlserver.pojo.result.topicServer.WaterfallResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "topic-server")
public interface TopicServer {

    @GetMapping("/topic-server/ping")
    void ping();

    @PostMapping("/topic-server/submit")
    CommonResult<String> submit(@RequestBody SubmitParam submitParam);

    @PostMapping("/topic-server/search")
    CommonResult<WaterfallResult> search(@RequestBody SearchParam searchParam);
    @PostMapping("/topic-server/finish")
    CommonResult<String> finish(@RequestBody FinishParam finishParam);

    @GetMapping("/topic-server/detail")
    CommonResult<DetailResult> detail(@RequestParam(value = "postId") Integer postId);

    @GetMapping("/topic-server/author")
    CommonResult<WaterfallResult> author(@RequestParam(value = "authorId") Integer authorId,
                                         @RequestParam(value = "lastTime") Long lastTime);

    @PostMapping("/topic-server/delete")
    CommonResult<String> delete(@RequestBody DeleteParam deleteParam);

}
