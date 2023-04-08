package com.sipc.topicserver.pojo.dto.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author o3141
 * @since 2023/4/3 21:34
 * @version 1.0
 */
@Data
public class SubmitParam implements Serializable {

    private String title;

    private String category;

    private List<String> categoryNext;

    private String context;

    private Integer gender;

    private Integer number;

    private String goTime;

    //LocalDateTime不可以，HttpMessageNotReadableException，need additional information such as an offset or time-zone
    // (see class Javadocs); nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException


}
