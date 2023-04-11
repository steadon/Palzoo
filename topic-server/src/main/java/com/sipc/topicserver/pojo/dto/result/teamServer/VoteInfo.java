package com.sipc.topicserver.pojo.dto.result.teamServer;

import lombok.Data;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/11 15:09
 */
@Data
public class VoteInfo {
    private Integer userId;
    private Boolean vote;
    private String userName;
}
