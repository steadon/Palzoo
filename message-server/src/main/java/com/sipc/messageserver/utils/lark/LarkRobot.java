package com.sipc.messageserver.utils.lark;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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

    private Entry entry;

//    @Resource
//    private

//    public void send() {
//
//    }

}
