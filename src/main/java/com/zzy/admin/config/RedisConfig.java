package com.zzy.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zzy
 * @description: TODO
 * @date 2025/2/27
 */
@Configuration
public class RedisConfig {

    /**
     * @param redisConnectionFactory 连接redis工厂
     * @return RedisTemplate<String, Object>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplateInit(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置序列化Key的实例化对象
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        /*设置Hash类型存储时，对象序列化报错解决*/

        // 重写key
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // 重写value
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
