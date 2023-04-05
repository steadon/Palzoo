package com.sipc.topicserver.pojo.dto.result;

import lombok.Data;

/**
 * ClassName searchResult
 * Description
 * Author o3141
 * Date 2023/4/4 9:33
 * Version 1.0
 */
@Data
public class Waterfall {

    private Integer postId;

    private String title;

    private String brief;

    private String authorName;
    
    private Integer watchNum;

    private String startTime;

    private String endTime;

}
