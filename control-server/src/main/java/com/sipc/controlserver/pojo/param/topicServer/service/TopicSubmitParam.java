package com.sipc.controlserver.pojo.param.topicServer.service;

import lombok.Data;

import java.util.List;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/9 16:47
 */
@Data
public class TopicSubmitParam {

    private Integer userId;

    private String title;

    private String category;

    private List<String> categoryNext;

    private String context;

    private Integer gender;

    private Integer number;

    private String goTime;

}
