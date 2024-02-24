package umc.liview.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    // JWT Token Template
    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

    // Caching Template
    @Bean(name = "cachingRedisTemplate")
    public RedisTemplate<String, Long> cachingRedisTemplate() {
        RedisTemplate<String, Long> cachingRedisTemplate = new RedisTemplate<>();
        cachingRedisTemplate.setKeySerializer(new StringRedisSerializer());
        cachingRedisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        cachingRedisTemplate.setConnectionFactory(redisConnectionFactory());

        return cachingRedisTemplate;
    }
}
