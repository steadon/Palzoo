package com.sipc.controlserver.pojo.result.topicServer;

import lombok.Data;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/8 21:57
 */
@Data
public class UserInfo {

    private String name;

    private String school;

    private Integer year;

    private String gender;

    private String avatarUrl;

}
