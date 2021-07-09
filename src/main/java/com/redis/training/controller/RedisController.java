package com.redis.training.controller;

import com.redis.training.controller.dto.RedisCrudResponseDto;
import com.redis.training.controller.dto.RedisCrudSaveRequestDto;
import com.redis.training.service.RedisCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
@Slf4j
public class RedisController {

    private final RedisCrudService redisCrudService;
    private final StringRedisTemplate redisTemplate;

    @GetMapping("/")
    public String ok() {
        return "ok";
    }

    @GetMapping("/keys")
    public String keys() {
        Set<String> keys = redisTemplate.opsForSet().members("*");
        if (Objects.isNull(keys)) throw new NullPointerException("keys");
        return Arrays.toString(keys.toArray());
    }

    @PostMapping("/cache")
    public Long save(@RequestBody RedisCrudSaveRequestDto requestDto) {
        log.info(">>>>>>> [SAVE] redisCrud={}", requestDto);
        return redisCrudService.save(requestDto);
    }

    @GetMapping("/cache/{id}")
    public RedisCrudResponseDto get(@PathVariable Long id) {
        return redisCrudService.get(id);
    }
}
