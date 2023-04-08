package com.sipc.controlserver.pojo.result.topicServer;

import lombok.Data;

import java.util.List;

/**
 * @author o3141
 * @since 2023/4/4 9:33
 * @version 1.0
 */
@Data
public class Waterfall {

    private Integer postId;

    private String title;

    private String category;

    private List<String> categoryNext;

    private String gender;

    private Integer number;

    private String goTime;

}
