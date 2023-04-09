package com.sipc.controlserver.pojo.param.teamServer;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostTeamParam {
    private String openId;
    private Integer userId;
    private Integer postId;
    private LocalDateTime endTime;
}
