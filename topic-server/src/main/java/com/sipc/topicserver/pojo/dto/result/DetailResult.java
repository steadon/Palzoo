package com.sipc.topicserver.pojo.dto.result;

import lombok.Data;

/**
 * @author o3141
 * @since 2023/4/4 14:39
 * @version 1.0
 */
@Data
public class DetailResult {

    private String title;

    private String content;

    private UserInfo author;

    private Integer watchNum;

    private String goTime;

    private Integer nowNum;

    private Byte isFinish;

}
