package com.sipc.controlserver.pojo.result.topicServer;

import lombok.Data;

/**
 * Author o3141
 * Date 2023/4/4 9:33
 * Version 1.0
 */
@Data
public class Waterfall {

    private Integer postId;

    private String title;

    private String brief;

    private String category;

    private String categoryNext;

    private Integer gender;

    private Integer number;

    private String goTime;

}