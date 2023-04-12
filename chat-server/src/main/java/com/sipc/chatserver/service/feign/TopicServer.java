package com.sipc.chatserver.service.feign;

import com.sipc.chatserver.pojo.CommonResult;
import com.sipc.chatserver.pojo.param.DeleteParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "topic-server")
public interface TopicServer {
    @PostMapping("/delete")
    CommonResult<String> delete(@RequestBody DeleteParam deleteParam);
}
