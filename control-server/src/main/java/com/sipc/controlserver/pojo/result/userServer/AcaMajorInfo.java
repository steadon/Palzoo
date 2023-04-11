package com.sipc.controlserver.pojo.result.userServer;

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
}
