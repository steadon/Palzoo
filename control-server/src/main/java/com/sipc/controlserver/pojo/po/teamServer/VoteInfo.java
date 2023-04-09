package com.sipc.controlserver.pojo.po.teamServer;

import lombok.Data;

@Data
public class VoteInfo {
    private Integer userId;
    private Boolean vote;
    private String userName;
}
