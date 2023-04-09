package com.sipc.topicserver.pojo.dto.result.loginServer;

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
