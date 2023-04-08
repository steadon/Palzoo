package com.sipc.topicserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//@Import(JacksonConfiguration.class)
public class TopicServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TopicServerApplication.class, args);
    }

}
