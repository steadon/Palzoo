package com.sipc.userserver.service;

import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.result.AcaMajorInfo;

import java.util.List;

public interface AcaMajorService {
    CommonResult<List<AcaMajorInfo>> getAllAcamajorInfo();
}
