package com.redis.training.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching // Cache 관련 어노테이션 이용 가능(public method 스캔 후 프록시 생성)
@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    public final ObjectMapper objectMapper;

    // RedisConfig 클래스에 스프링빈으로 등록된 커넥션팩토리를 주입받음
    public final RedisConnectionFactory connectionFactory;

    /**
    * 캐싱을 할 때 로컬 캐시가 아닌 Redis를 이용하도록 설정
    */
    @Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofSeconds(30)); // 캐시 유효기간(Time to live)

        // Connect(connectionFactory), 캐시설정(redisCacheConfiguration)과 관련된 것들을 적용
        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(redisCacheConfiguration).build();

        return redisCacheManager;
    }

}
