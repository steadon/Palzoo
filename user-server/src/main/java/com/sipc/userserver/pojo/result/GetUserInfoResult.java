package com.sipc.userserver.pojo.result;

import lombok.Data;

@Data
public class GetUserInfoResult {
    private Integer userId;
    private String username;
    private String academy;
    private String major;
}
