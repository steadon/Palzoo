package com.sipc.messageserver.aop;

import lombok.Data;

import java.net.URI;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 20:28
 */
@Data
public class WebLog {
    /**
     * 操作描述
     */
    private String description;
    /**
     * 操作用户
     */
    private String username;
    /**
     * 操作时间
     */
    private String startTime;
    /**
     * 消耗时间
     */
    private Long spendTime;

    /**
     * URI
     */
    private URI uri;

    private String url;

    /**
     * 请求类型
     */
    private String method;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 请求参数
     */
    private Object parameter;
    /**
     * 请求返回的结果
     */
    private Object result;

}
