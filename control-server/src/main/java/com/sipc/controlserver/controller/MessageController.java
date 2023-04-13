package com.sipc.controlserver.controller;

import com.sipc.controlserver.pojo.CommonResult;
import com.sipc.controlserver.pojo.param.messageServer.SendParam;
import com.sipc.controlserver.pojo.result.messageServer.MessageResult;
import com.sipc.controlserver.service.feign.LoginServer;
import com.sipc.controlserver.service.feign.MessageServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 17:13
 */
@CrossOrigin
@RestController
@RequestMapping("/control")
@Slf4j
public class MessageController {

    @Resource
    private MessageServer messageServer;

    @Resource
    private LoginServer loginServer;

    @PostMapping("/message/send")
    public CommonResult<String> send(@RequestBody SendParam sendParam) {
        return messageServer.send(sendParam);
    }

    @GetMapping("/message/get")
    public CommonResult<List<MessageResult>> getMessage(@RequestParam String openId) {

        Integer userId;

        //鉴权
        try {
            userId = loginServer.getUser(openId).getId();
        } catch (RuntimeException e) {
            log.warn("用户权限异常,用户openid: {}, 错误信息: {}", openId, e.toString());
            return CommonResult.fail("权限异常");
        }

        return messageServer.getMessage(userId);
    }
}
