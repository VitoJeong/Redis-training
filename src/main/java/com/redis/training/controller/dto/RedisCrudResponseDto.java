package com.redis.training.controller.dto;

import com.redis.training.domain.RedisCrud;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RedisCrudResponseDto {
    private Long id;
    private String description;
    private LocalDateTime updatedAt;

    public RedisCrudResponseDto(RedisCrud redisHash) {
        this.id = redisHash.getId();
        this.description = redisHash.getDescription();
        this.updatedAt = redisHash.getUpdatedAt();
    }
}
