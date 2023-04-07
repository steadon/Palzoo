package com.sipc.loginserver.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class WechatCommonUtil implements InitializingBean {
    @Value("${wechat.appId}")
    private String appId;
    @Value("${wechat.appSecret}")
    private String appSecret;

    public static String APP_ID;
    public static String APP_SECRET;

    @Override
    public void afterPropertiesSet() {
        APP_ID = this.appId;
        APP_SECRET = this.appSecret;
    }
}
