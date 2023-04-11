package com.sipc.userserver.pojo.result;

import com.sipc.userserver.pojo.domain.AcaMajor;
import lombok.Data;

/**
 * 查询学院专业信息的返回结果
 * @author DoudiNCer
 */
@Data
public class AcaMajorInfo {
    // 学院专业ID
    private Integer id;
    // 学院名称
    private String acaName;
    // 专业名称
    private String majorName;
    public AcaMajorInfo(){}
    public AcaMajorInfo(AcaMajor acaMajor){
        this.setId(acaMajor.getId());
        this.setMajorName(acaMajor.getMajorName());
        this.setAcaName(acaMajor.getAcaName());
    }
}
