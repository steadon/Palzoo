package com.sipc.topicserver.pojo.dto.result;

import lombok.Data;

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

    private String categoryNext;

    private Integer gender;

    private Integer number;

    private String goTime;

}
