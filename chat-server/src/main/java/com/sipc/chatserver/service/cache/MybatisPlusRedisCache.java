package com.sipc.chatserver.service.cache;

import org.apache.ibatis.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

public class MybatisPlusRedisCache implements Cache {

    private final String id;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public MybatisPlusRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        // 缓存实现代码
        redisTemplate.opsForValue().set(key, value);
        //随机数策略防止缓存雪崩
        redisTemplate.expire(key, 1440 * 60 + new Random().nextInt(10), TimeUnit.SECONDS);
    }

    @Override
    public Object getObject(Object key) {
        // 缓存实现代码
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Object removeObject(Object key) {
        // 缓存实现代码
        return redisTemplate.delete(key);
    }

    @Override
    public void clear() {
        // 缓存实现代码
        Set<Object> cacheKeys = redisTemplate.keys("*");
        if (cacheKeys != null) {
            redisTemplate.delete(cacheKeys);
        }
    }

    @Override
    public int getSize() {
        // 缓存实现代码
        Set<Object> cacheKeys = redisTemplate.keys("*");
        return cacheKeys != null ? cacheKeys.size() : 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        // 缓存实现代码
        return null;
    }
}
