package com.sipc.controlserver.pojo.param.topicServer;

import lombok.Data;

import java.util.Map;

/**
 * @author o3141
 * @since 2023/4/3 22:55
 * @version 1.0
 */
@Data
public class SearchParam {

    private Long lastTime;

    private String startTime;

    private String endTime;

    private Map<String, String> screen;

}
