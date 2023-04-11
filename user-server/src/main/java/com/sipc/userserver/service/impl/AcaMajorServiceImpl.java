package com.sipc.userserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sipc.userserver.mapper.AcaMajorMapper;
import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.domain.AcaMajor;
import com.sipc.userserver.pojo.result.AcaMajorInfo;
import com.sipc.userserver.service.AcaMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AcaMajorServiceImpl implements AcaMajorService {
    private final AcaMajorMapper acaMajorMapper;

    @Autowired
    public AcaMajorServiceImpl(AcaMajorMapper acaMajorMapper) {
        this.acaMajorMapper = acaMajorMapper;
    }

    /**
     * 获取所有学院专业
     *
     * @return 所有学院与专业，包括学院专业ID、学院名、专业名
     * @author DoudiNCer
     */
    @Override
    public CommonResult<List<AcaMajorInfo>> getAllAcamajorInfo() {
        List<AcaMajorInfo> result = new ArrayList<>();
        List<AcaMajor> acaMajors = acaMajorMapper.selectList(new QueryWrapper<>());
        for (AcaMajor acaMajor : acaMajors) {
            AcaMajorInfo info = new AcaMajorInfo();
            info.setId(acaMajor.getId());
            info.setAcaName(acaMajor.getAcaName());
            info.setMajorName(acaMajor.getMajorName());
            result.add(info);
        }
        return CommonResult.success(result);
    }
}
