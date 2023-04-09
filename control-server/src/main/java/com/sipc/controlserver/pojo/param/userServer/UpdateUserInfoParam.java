package com.sipc.controlserver.pojo.param.userServer;

import lombok.Data;

@Data
public class UpdateUserInfoParam {
    private String openId;
    private Integer userId;
    private String userName;
    private Integer acaMajorId;
    private Boolean gender;
    private String phone;
}
