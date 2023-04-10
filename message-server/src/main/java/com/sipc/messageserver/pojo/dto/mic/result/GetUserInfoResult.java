package com.sipc.messageserver.pojo.dto.mic.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * ClassName GetUserInfoResult
 * Description
 * Author o3141
 * Date 2023/4/4 17:36
 * Version 1.0
 */
@Data
@AllArgsConstructor
public class GetUserInfoResult implements Serializable {
    private Integer userId;
    private String username;
    private String academy;
    private String major;
    private String gender;
    private String phone;

    public GetUserInfoResult() {}

}
