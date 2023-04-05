package com.sipc.topicserver.pojo.dto.param;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName SearchParam
 * @Description TODO
 * @Author o3141
 * @Date 2023/4/3 22:55
 * @Version 1.0
 */
@Data
public class SearchParam {

    private Long lastTime;

    private String startTime;

    private String endTime;

    private Map<String, String> screen;

}
