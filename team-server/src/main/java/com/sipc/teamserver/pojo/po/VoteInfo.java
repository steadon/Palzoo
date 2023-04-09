package com.sipc.teamserver.pojo.po;

import lombok.Data;

@Data
public class VoteInfo {
    private Integer userId;
    private Boolean vote;
    private String userName;
}
