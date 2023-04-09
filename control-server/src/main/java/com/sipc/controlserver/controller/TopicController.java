package com.sipc.controlserver.controller;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.LevelParam;
import com.sipc.controlserver.pojo.param.topicServer.*;
import com.sipc.controlserver.pojo.param.topicServer.service.TopicDeleteParam;
import com.sipc.controlserver.pojo.param.topicServer.service.TopicFinishParam;
import com.sipc.controlserver.pojo.param.topicServer.service.TopicSubmitParam;
import com.sipc.controlserver.pojo.result.topicServer.DetailNumResult;
import com.sipc.controlserver.pojo.result.topicServer.DetailResult;
import com.sipc.controlserver.pojo.result.topicServer.WaterfallResult;
import com.sipc.controlserver.pojo.resultEnum.ResultEnum;
import com.sipc.controlserver.service.feign.LoginServer;
import com.sipc.controlserver.service.feign.TopicServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


/**
 * @author o3141
 * @version 1.0
 * @since 2023/4/5 9:19
 */
@CrossOrigin
@RestController()
@RequestMapping("/controller/topic")
@Slf4j
public class TopicController {

    @Resource
    private TopicServer topicServer;

    @Resource
    private LoginServer loginServer;

    @GetMapping("/ping")
    public void ping() {
        System.out.println("controller");
        topicServer.ping();
    }

    @PostMapping("/submit")
    public CommonResult<String> submit(@RequestBody SubmitParam submitParam) {

        Integer userId;

        //鉴权
        try {
            userId = loginServer.getUser(submitParam.getOpenId()).getId();
        } catch (RuntimeException e) {
            log.warn("完成帖子接口不正常,用户openid: {}, 错误信息: {}", submitParam.getOpenId(), e.toString());
            return CommonResult.fail("权限异常");
        }

        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange

        TopicSubmitParam topicSubmitParam = new TopicSubmitParam();
        topicSubmitParam.setUserId(userId);
        topicSubmitParam.setTitle(submitParam.getTitle());
        topicSubmitParam.setCategory(submitParam.getCategory());
        topicSubmitParam.setCategoryNext(submitParam.getCategoryNext());
        topicSubmitParam.setContext(submitParam.getContext());
        topicSubmitParam.setGender(submitParam.getGender());
        topicSubmitParam.setNumber(submitParam.getNumber());
        topicSubmitParam.setGoTime(submitParam.getGoTime());

        return topicServer.submit(topicSubmitParam);

//        return CommonResult.success("提交成功，审核中");
    }

    @PostMapping("/search")
    public CommonResult<WaterfallResult> search(@RequestBody SearchParam searchParam) {

        return topicServer.search(searchParam);
    }

    @PostMapping("/finish")
    public CommonResult<String> finish(@RequestBody FinishParam finishParam) {

        Integer userId;

        //鉴权
        try {
            userId = checkRole(finishParam.getOpenId());
        } catch (RuntimeException e) {
            log.warn("完成帖子接口不正常,错误信息: {}", e.toString());
            return CommonResult.fail("权限异常");
        }

        TopicFinishParam topicFinishParam = new TopicFinishParam();
        topicFinishParam.setUserId(userId);
        topicFinishParam.setPostId(finishParam.getPostId());

        return topicServer.finish(topicFinishParam);
    }

    @GetMapping("/detail")
    public CommonResult<DetailResult> detail(@RequestParam Integer postId) {
        return topicServer.detail(postId);
    }

    @GetMapping("/author")
    public CommonResult<WaterfallResult> author(@RequestParam Integer authorId, @RequestParam(required = false) Long lastTime) {
        return topicServer.author(authorId, lastTime);
    }

    @PostMapping("/delete")
    public CommonResult<String> delete(@RequestBody DeleteParam deleteParam) {

        Integer userId;

        //鉴权
        try {
            userId = checkRole(deleteParam.getOpenId());
        } catch (RuntimeException e) {
            log.warn("删除帖子接口不正常,错误信息: {}", e.toString());
            return CommonResult.fail("权限异常");
        }

        TopicDeleteParam topicDeleteParam = new TopicDeleteParam();
        topicDeleteParam.setUserId(userId);
        topicDeleteParam.setPostId(deleteParam.getPostId());

        return topicServer.delete(topicDeleteParam);
    }

    @PostMapping("/delay")
    public CommonResult<String> delay(@RequestBody DelayParam delayParam) {
        return topicServer.delay(delayParam);
    }

    @GetMapping("/detail/num")
    public CommonResult<DetailNumResult> detailNum(@RequestParam Integer postId) {
        return topicServer.detailNum(postId);
    }

    @GetMapping("/check/is/author")
    public CommonResult<IsAuthorResult> isAuthor(@RequestParam Integer userId, @RequestParam Integer postId) {
        return topicServer.isAuthor(userId, postId);
    }

    private Integer checkRole(String openId) throws RuntimeException{
        CommonResult<LevelParam> levelParamCommonResult;

        levelParamCommonResult = loginServer.checkRole(openId);

        Integer userId;

        if (Objects.equals(levelParamCommonResult.getCode(), ResultEnum.SUCCESS.getCode())
                && levelParamCommonResult.getData().getLevel() == 1)  {
            userId = 0;
        } else {
            userId = loginServer.getUser(openId).getId();
        }
        return userId;
    }



}
