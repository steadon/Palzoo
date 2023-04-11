package com.sipc.chatserver.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 的配置类，后期可以自定义负载均衡策略
 *
 * @author Sterben
 * @version 1.0.0
 */
@Configuration
public class MyRestTemplate {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


