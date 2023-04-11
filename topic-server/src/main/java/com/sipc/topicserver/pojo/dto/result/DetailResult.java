package com.sipc.topicserver.pojo.dto.result;

import lombok.Data;

import java.util.List;

/**
 * @author o3141
 * @since 2023/4/4 14:39
 * @version 1.0
 */
@Data
public class DetailResult {

    private String title;

    private String content;

    private String category;

    private List<String> categoryNext;

    private UserInfo author;

    private Integer watchNum;

    private Integer num;

    private String gender;

    private String goTime;

    private Integer nowNum;

    private Byte isFinish;

}
