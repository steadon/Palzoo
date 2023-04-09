package com.sipc.controlserver.service.feign;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.topicServer.*;
import com.sipc.controlserver.pojo.param.topicServer.service.TopicDeleteParam;
import com.sipc.controlserver.pojo.param.topicServer.service.TopicFinishParam;
import com.sipc.controlserver.pojo.param.topicServer.service.TopicSubmitParam;
import com.sipc.controlserver.pojo.result.topicServer.DetailNumResult;
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
    CommonResult<String> submit(@RequestBody TopicSubmitParam submitParam);

    @PostMapping("/topic-server/search")
    CommonResult<WaterfallResult> search(@RequestBody SearchParam searchParam);
    @PostMapping("/topic-server/finish")
    CommonResult<String> finish(@RequestBody TopicFinishParam finishParam);

    @GetMapping("/topic-server/detail")
    CommonResult<DetailResult> detail(@RequestParam(value = "postId") Integer postId);

    @GetMapping("/topic-server/author")
    CommonResult<WaterfallResult> author(@RequestParam(value = "authorId") Integer authorId,
                                         @RequestParam(value = "lastTime") Long lastTime);

    @PostMapping("/topic-server/delete")
    CommonResult<String> delete(@RequestBody TopicDeleteParam deleteParam);

    @PostMapping("/topic-server/delay")
    CommonResult<String> delay(@RequestBody DelayParam delayParam);

    @GetMapping("/topic-server/check/detail/num")
    CommonResult<DetailNumResult> detailNum(@RequestParam(value = "postId") Integer postId);

    @GetMapping("/topic-sever/check/is/author")
    CommonResult<IsAuthorResult> isAuthor(@RequestParam(value = "userId") Integer userId,
                                          @RequestParam(value = "postId") Integer postId);
}
