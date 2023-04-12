package com.sipc.messageserver.service.opfeign;


import com.sipc.messageserver.pojo.dto.CommonResult;
import com.sipc.messageserver.pojo.dto.result.topicServer.DetailResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "topic-server")
public interface TopicServer {


    @GetMapping("/topic-server/detail")
    CommonResult<DetailResult> detail(@RequestParam(value = "postId") Integer postId);

}
