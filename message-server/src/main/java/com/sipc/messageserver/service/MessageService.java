package com.sipc.messageserver.service;

import com.sipc.messageserver.pojo.dto.CommonResult;
import com.sipc.messageserver.pojo.dto.param.SendParam;
import com.sipc.messageserver.pojo.dto.result.MessageResult;

import java.util.List;

public interface MessageService {

    CommonResult<String> send(SendParam sendParam);

   CommonResult<List<MessageResult>> getMessage(Integer userId);

}
