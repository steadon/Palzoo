package com.sipc.controlserver.pojo.param.loginServer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LevelParam {
    private Integer level;
    private String description;

    public LevelParam() {
    }
}
