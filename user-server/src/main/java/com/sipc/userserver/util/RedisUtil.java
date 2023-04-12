package com.sipc.userserver.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;

    public RedisUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 写入String缓存
     * @param key 键
     * @param value 值
     * @return boolean
     */
    public boolean set(final String key,Object value){
        boolean result = false;
        try {
            ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            result = true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 设置缓存过期时间
     * @param key 键
     * @param value 值
     * @param expireTime 过期时间
     * @param timeUnit timeUnit
     * @return boolean
     */
    public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit){
        boolean result = false;
        try {
            ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            redisTemplate.expire(key,expireTime,timeUnit);
            result = true;
        } catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 判断缓存是否存在
     * @param key 键
     * @return boolean
     */
    public boolean exists(final String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     * @param key 键
     */
    public void remove(final String key){
        if (exists(key)){
            redisTemplate.delete(key);
        }
    }

    /**
     * 取出缓存
     * @param key 键
     * @return 缓存
     */
    public Object get(final String key){
        Object result = null;
        ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);

        return result;
    }

    public Long incrWatchNum(final String key, Long defaultValue) {

        //判断有没有watch_num字段
        Object o = this.get(key);
        if (o == null) {
            return this.incrLong(key, defaultValue);
        }
        else {
            return redisTemplate.opsForValue().increment(key);
        }
    }

    public Long incrLong(final String key, Long delta) {
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public String getUserIdKey(int userId){
        return "USERINFO" + userId + "REDISCACHE";
    }

}
