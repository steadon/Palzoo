package com.sipc.loginserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DropUserInfoParam {
    private Integer userId;

    public DropUserInfoParam() {

    }
}
