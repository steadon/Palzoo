package com.sipc.controlserver.controller;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.topicServer.*;
import com.sipc.controlserver.pojo.result.topicServer.DetailResult;
import com.sipc.controlserver.pojo.result.topicServer.WaterfallResult;
import com.sipc.controlserver.service.feign.TopicServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author o3141
 * @since 2023/4/5 9:19
 * @version 1.0
 */
@RestController()
@RequestMapping("/controller/topic")
public class TopicController {

    @Resource
    private TopicServer topicServer;

    @GetMapping("/ping")
    public void ping() {
        System.out.println("controller");
        topicServer.ping();
    }

    @PostMapping("/submit")
    public CommonResult<String> submit(@RequestBody SubmitParam submitParam) {

        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        return topicServer.submit(submitParam);

//        return CommonResult.success("提交成功，审核中");
    }

    @PostMapping("/search")
    public CommonResult<WaterfallResult> search(@RequestBody SearchParam searchParam) {

        return topicServer.search(searchParam);
    }

    @PostMapping("/finish")
    public CommonResult<String> finish(@RequestBody FinishParam finishParam) {
        return topicServer.finish(finishParam);
    }

    @GetMapping("/detail")
    public CommonResult<DetailResult> detail(@RequestParam Integer postId) {
        return topicServer.detail(postId);
    }

    @GetMapping("/author")
    public CommonResult<WaterfallResult> author(@RequestParam Integer authorId, @RequestParam Long lastTime) {
        return topicServer.author(authorId, lastTime);
    }

    @PostMapping("/delete")
    public CommonResult<String> delete(@RequestBody DeleteParam deleteParam) {
        return topicServer.delete(deleteParam);
    }

    @PostMapping("/delay")
    public CommonResult<String> delay(@RequestBody DelayParam delayParam) {
        return topicServer.delay(delayParam);
    }

}
