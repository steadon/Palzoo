package com.sipc.topicserver.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sipc.topicserver.pojo.dto.mic.result.GetUserInfoResult;
import com.sipc.topicserver.pojo.dto.param.SubmitParam;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Redis 缓存配置类
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);


        serializer.setObjectMapper(redisObjectMapper());

        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();

        template.expire("mykey", 60, TimeUnit.MINUTES);

        return template;
    }
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(10));

        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(config).build();
    }

    /**
     * 注册JavaTimeModule模块，并配置ObjectMapper以将Java 8日期时间类型序列化为JSON字符串
     * 用@bean的话会将ObjectMapper注册到spring容器中，导致全局序列化都采用这个配置
     */
//    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule()); // 注册JavaTimeModule模块
        //通过将WRITE_DATES_AS_TIMESTAMPS设置为false，它将告诉ObjectMapper将日期时间类型序列化为ISO-8601格式的字符串。
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // ObjectMapper 在序列化时默认会将单个对象包装在一个包含该对象的数组中,可能会导致序列化json对象为json数组
        /*
        ObjectMapper 被配置为启用默认类型，并且该类型是非最终类型，因此它会将序列化的对象的类型信息包含在序列化后的JSON字符串中。
          因此，在序列化单个对象时，它可能会将对象包装在一个数组中，以便在反序列化时能够还原对象的类型信息。
          */
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_OBJECT
        );

        objectMapper.registerSubtypes(GetUserInfoResult.class, SubmitParam.class);

        //使用objectMapper.registerSubtypes()方法将所有可能的子类型显式注册到ObjectMapper中
//        objectMapper.registerSubtypes(Post.class, GetUserInfoResult.class, SubmitParam.class);

//        objectMapper.setDefaultTyping(new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.NON_FINAL) {
//            @Override
//            public boolean useForType(JavaType t) {
//                return true;
//            }
//        }.init(JsonTypeInfo.Id.CLASS, null)
//                .inclusion(JsonTypeInfo.As.EXISTING_PROPERTY));

//        objectMapper.disableDefaultTyping();
        //配置Jackson2JsonRedisSerializer序列化策略
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);


        return objectMapper;
    }


}
