package com.sipc.topicserver.controller;

import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.result.DetailNumResult;
import com.sipc.topicserver.pojo.dto.result.IsAuthorResult;
import com.sipc.topicserver.service.CheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/9 16:25
 */
@RestController
@RequestMapping("/topic-server/check")
public class CheckController {

    @Resource
    private CheckService checkService;

    @GetMapping("/detail/num")
    public CommonResult<DetailNumResult> detailNum(@RequestParam Integer postId) {
        return checkService.detailNum(postId);
    }

    @GetMapping("/is/author")
    public CommonResult<IsAuthorResult> isAuthor(@RequestParam Integer userId, @RequestParam Integer postId) {
        return checkService.isAuthor(userId, postId);
    }

}
