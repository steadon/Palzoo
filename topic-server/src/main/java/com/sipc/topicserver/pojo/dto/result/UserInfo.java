package com.sipc.topicserver.pojo.dto.result;

import lombok.Data;

/**
 * 用户信息类，嵌入帖子详细类
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


}
