package com.nowcoder.communityy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);//设置工厂，就可以访问数据库了

        /*
            配置Template就是配置序列化的方式，我们写的程序时Java程序，数据时Java类型的数据，最后
            我们要把数据存到redis数据库中，所以我们要使用序列化的方式
        */

        // 设置key的序列化方式, 序列化成String格式
        template.setKeySerializer(RedisSerializer.string());
        // 设置value的序列化方式，序列化成JSON格式，JSON时结构化的数据格式，恢复时也比较方便读取
        template.setValueSerializer(RedisSerializer.json());
        // 设置hash的key的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        // 设置hash的value的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();//生效
        return template;
    }
}
