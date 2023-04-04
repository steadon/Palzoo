package com.sipc.userserver.pojo.result;

import lombok.Data;

import javax.annotation.ParametersAreNonnullByDefault;

@Data
public class AcaMajorInfo {
    private Integer id;
    private String acaName;
    private String majorName;
}
