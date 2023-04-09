package com.sipc.teamserver.pojo.result.userServer;

import lombok.Data;

@Data
public class GetUserInfoResult {
    private Integer userId;
    private String username;
    private String academy;
    private String major;
    private String gender;
    private String phone;
}
