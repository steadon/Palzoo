package com.sipc.loginserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LevelParam {
    private Integer level;
    private String description;

    public LevelParam() {
        //该无参构造用于反序列化，不可删除
    }
}
