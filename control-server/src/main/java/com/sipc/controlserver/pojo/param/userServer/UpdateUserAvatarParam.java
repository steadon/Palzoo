package com.sipc.controlserver.pojo.param.userServer;

import lombok.Data;

@Data
public class UpdateUserAvatarParam {
    private Integer userId;
    private String openId;
}
