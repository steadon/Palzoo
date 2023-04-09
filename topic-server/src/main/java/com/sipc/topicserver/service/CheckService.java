package com.sipc.topicserver.service;

import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.result.DetailNumResult;
import com.sipc.topicserver.pojo.dto.result.IsAuthorResult;

public interface CheckService {

    CommonResult<DetailNumResult> detailNum(Integer postId);

    CommonResult<IsAuthorResult> isAuthor(Integer userId, Integer postId);

}
