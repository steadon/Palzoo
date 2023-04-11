package com.sipc.messageserver.controller;

import com.sipc.messageserver.pojo.dto.CommonResult;
import com.sipc.messageserver.pojo.dto.param.SendParam;
import com.sipc.messageserver.pojo.dto.result.MessageResult;
import com.sipc.messageserver.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 10:35
 */
@RestController
@RequestMapping("/message-server")
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/send")
    public CommonResult<String> send(@RequestBody SendParam sendParam) {
        return messageService.send(sendParam);
    }

    @GetMapping("/get/message")
    public CommonResult<List<MessageResult>> getMessage(@RequestParam Integer userId) {
        return messageService.getMessage(userId);
    }

}
