package com.sipc.controlserver.pojo.result.topicServer;

import lombok.Data;

/**
 * @author o3141
 * @since 2023/4/4 14:39
 * @version 1.0
 */
@Data
public class DetailResult {

    private String title;

    private String brief;

    private String content;

    private String authorName;

    private Integer watchNum;

    private String stopTime;

    private String startTime;

    private String endTime;

    private Byte isFinish;

}
