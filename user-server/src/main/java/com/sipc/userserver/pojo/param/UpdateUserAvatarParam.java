package com.sipc.userserver.pojo.param;

import lombok.Data;

@Data
public class UpdateUserAvatarParam {
    private Integer userId;
    private String openId;
}
