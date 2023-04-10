package com.sipc.topicserver.service;

import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.param.*;
import com.sipc.topicserver.pojo.dto.result.DetailResult;
import com.sipc.topicserver.pojo.dto.result.WaterfallResult;

/**
 * ClassName TopicServer
 * Description
 * Author o3141
 * Date 2023/4/3 22:08
 * Version 1.0
 */
public interface TopicService {

    CommonResult<String> submit(SubmitParam submitParam);

    CommonResult<WaterfallResult> search(SearchParam searchParam);

    CommonResult<String> finish(FinishParam finishParam);

    CommonResult<DetailResult> detail(Integer postId);

    CommonResult<WaterfallResult> author(Integer authorId, Long lastTime);

    CommonResult<String> delete(DeleteParam deleteParam);

    CommonResult<String> delay(DelayParam delayParam);



}
