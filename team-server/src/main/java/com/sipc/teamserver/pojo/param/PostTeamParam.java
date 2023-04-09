package com.sipc.teamserver.pojo.param;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostTeamParam {
    private Integer postId;
    private LocalDateTime endTime;
}
