package com.sipc.controlserver.pojo.param.teamServer;

import lombok.Data;

@Data
public class PostVoteParam {
    private Integer userId;
    private Integer teamId;
    private Boolean vote;
}
