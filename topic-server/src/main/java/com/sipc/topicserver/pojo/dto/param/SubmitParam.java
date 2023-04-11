package com.sipc.topicserver.pojo.dto.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 提交帖子接口的参数类，保存了提交帖子所需的相关信息
 * @author o3141
 * @since 2023/4/3 21:34
 * @version 1.0
 */
@Data
public class SubmitParam implements Serializable {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 分类
     */
    private String category;

    /**
     * 自定义标签列表
     */
    private List<String> categoryNext;

    /**
     * 帖子的文本内容
     */
    private String context;

    /**
     * 帖子所发出活动的性别限制
     */
    private Integer gender;

    /**
     * 帖子所发出活动的期望人数
     */
    private Integer number;

    /**
     * 集合时间
     */
    private String goTime;

    //LocalDateTime不可以，HttpMessageNotReadableException，need additional information such as an offset or time-zone
    // (see class Javadocs); nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException


}
