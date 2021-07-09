package com.redis.training.service;

import com.redis.training.controller.dto.RedisCrudResponseDto;
import com.redis.training.controller.dto.RedisCrudSaveRequestDto;
import com.redis.training.domain.RedisCrud;
import com.redis.training.domain.RedisCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RedisCrudService {

    private final RedisCrudRepository redisCrudRepository;

    @Transactional
    public Long save(RedisCrudSaveRequestDto requestDto) {
        return redisCrudRepository.save(requestDto.toRedisHash()).getId();
    }

    public RedisCrudResponseDto get(Long id) {
        RedisCrud redisCrud = redisCrudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nothing saved. id = " + id));
        return new RedisCrudResponseDto(redisCrud);
    }
}
