package com.sipc.chatserver.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.sipc.chatserver.service.cache.MybatisPlusRedisCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {
    @Bean
    public MybatisPlusRedisCache mybatisPlusRedisCache() {
        return new MybatisPlusRedisCache("com.sipc.chatserver.service.cache.MybatisPlusRedisCache");
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.addCache(mybatisPlusRedisCache());
    }
}
