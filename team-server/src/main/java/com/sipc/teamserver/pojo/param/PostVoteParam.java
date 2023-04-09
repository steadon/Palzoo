package com.sipc.teamserver.pojo.param;

import lombok.Data;

@Data
public class PostVoteParam {
    private Integer userId;
    private Integer teamId;
    private Boolean vote;
}
