package com.sipc.userserver.service;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.result.AcaMajorInfo;

import java.util.List;

public interface AcaMajorService {
    /**
     * 获取所有学院专业
     * @return 所有学院与专业，包括学院专业ID、学院名、专业名
     * @author DoudiNCer
     */
    CommonResult<List<AcaMajorInfo>> getAllAcamajorInfo();
}
