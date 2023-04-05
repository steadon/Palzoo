package com.sipc.topicserver.pojo.dto.mic.result;

import lombok.Data;

/**
 * ClassName GetUserInfoResult
 * Description
 * Author o3141
 * Date 2023/4/4 17:36
 * Version 1.0
 */
@Data
public class GetUserInfoResult {

    private Integer userId;
    private String username;
    private String academy;
    private String major;

}
