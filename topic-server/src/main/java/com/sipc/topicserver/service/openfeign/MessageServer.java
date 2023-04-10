package com.sipc.topicserver.service.openfeign;

import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.param.messageServer.SendParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("message-server")
public interface MessageServer {

    @PostMapping("/message-server/send")
    CommonResult<String> send(@RequestBody SendParam sendParam);

}
