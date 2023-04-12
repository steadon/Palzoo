package com.sipc.messageserver.utils.lark;

import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 14:57
 */
@ConfigurationProperties("lark-robot")
@Component
@Data
public class LarkRobot {

    private Boolean dev;

    private String url;

    private static Entry entry;

//
//    @Resource
//    private RestTemplate restTemplate;

    public void send(JoinPoint joinPoint, Exception exception) {
//        joinPoint
    }

}
