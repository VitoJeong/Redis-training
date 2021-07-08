package com.redis.training.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    /**
     * RedisTemplate (RedisAutoConfiguration은 RedisTemplate, StringRedisTemplate 두 가지 bean 생성)
     * Redis 서버에 Redis Command 수행을 위한 객체(데이터 CRUD를 위한 인터페이스 제공 high-level)
     *  Object serialization 과 connection management 수행 (thread-safe)
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // String
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // Json
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * RedisConnectionFactory
     * Redis 서버와 통신 관련 객체.(통신 추상화를 제공 low-level)
     * HostName, Port 등을 지정하고 드라이버 모듈(Lettuce)까지 지정.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 단일 노드 Redis와 연결하는 RedisConnection을 설정하는 데 필요한 클래스
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return connectionFactory;
    }


}
