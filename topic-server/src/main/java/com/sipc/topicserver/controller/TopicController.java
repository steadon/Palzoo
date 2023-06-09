package com.sipc.topicserver.controller;

import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.param.*;
import com.sipc.topicserver.pojo.dto.result.DetailResult;
import com.sipc.topicserver.pojo.dto.result.WaterfallResult;
import com.sipc.topicserver.service.TopicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * ClassName TopicController
 * Description 关于帖子的操作
 * Author tzih
 * Date 2023/4/3 20:27
 * Version 1.0
 */
@RestController
@RequestMapping("/topic-server")
public class TopicController {

    @Resource
    private TopicService topicService;

    
    @GetMapping("/ping")
    public void ping() {
        System.out.println("ahlasas");
    }

    @PostMapping("/submit")
    public CommonResult<String> submit(@RequestBody SubmitParam submitParam) {

        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        return topicService.submit(submitParam);

//        return CommonResult.success("提交成功，审核中");
    }

    @PostMapping("/search")
    public CommonResult<WaterfallResult> search(@RequestBody SearchParam searchParam) {

        return topicService.search(searchParam);
    }

    @PostMapping("/finish")
    public CommonResult<String> finish(@RequestBody FinishParam finishParam) {
        return topicService.finish(finishParam);
    }

    @GetMapping("/detail")
    public CommonResult<DetailResult> detail(@RequestParam Integer postId) {
        return topicService.detail(postId);
    }

    @GetMapping("/author")
    public CommonResult<WaterfallResult> author(@RequestParam Integer authorId) {
        return topicService.author(authorId);
    }

    @PostMapping("/delete")
    public CommonResult<String> delete(@RequestBody DeleteParam deleteParam) {
        return topicService.delete(deleteParam);
    }

    @PostMapping("/delay")
    public CommonResult<String> delay(@RequestBody DelayParam delayParam) {
        return topicService.delay(delayParam);
    }



}
