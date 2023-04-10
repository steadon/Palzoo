package com.sipc.controlserver.service.feign;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.messageServer.SendParam;
import com.sipc.controlserver.pojo.result.messageServer.MessageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 17:08
 */
@FeignClient("message-server")
public interface MessageServer {

    @PostMapping("/message-server/send")
    CommonResult<String> send(@RequestBody SendParam sendParam);

    @GetMapping("/message-server/get/message")
    CommonResult<List<MessageResult>> getMessage(@RequestParam("userId") Integer userId);
}
