package com.sipc.controlserver.pojo.param.userServer;

import lombok.Data;

@Data
public class DropUserInfoParam {
    private String openId;
    private Integer userId;
}
