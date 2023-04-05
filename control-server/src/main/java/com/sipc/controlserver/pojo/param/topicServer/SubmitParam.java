package com.sipc.controlserver.pojo.param.topicServer;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SubmitParam
 * @Description submit的参数类
 * @Author o3141
 * @Date 2023/4/3 21:34
 * @Version 1.0
 */
@Data
public class SubmitParam implements Serializable {

    private String title;

    private String category;

    private String brief;

    private String context;

    //LocalDateTime不可以，HttpMessageNotReadableException，need additional information such as an offset or time-zone
    // (see class Javadocs); nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException

    private String startTime;

    private String endTime;

}
