package com.sipc.userserver.pojo.param;

import lombok.Data;

@Data
public class UpdateUserInfoParam {
    private Integer userId;
    private String userName;
    private Integer acaMajorId;
}
