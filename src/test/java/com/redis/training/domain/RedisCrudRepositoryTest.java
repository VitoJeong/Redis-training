package com.redis.training.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
class RedisCrudRepositoryTest {

    @Autowired
    private RedisCrudRepository redisCrudRepository;

    LocalDateTime updatedAt = LocalDateTime.now();

    @AfterEach
    public void tearDown() throws Exception{
        redisCrudRepository.deleteAll();
    }

    @Test
    @DisplayName("기본 등록 및 조회")
    public void get() {

        // GIVEN
        Long id = 0l;
        String description = "description";

        RedisCrud redisCrud = RedisCrud.builder()
                .id(id)
                .description(description)
                .updatedAt(updatedAt)
                .build();

        // WHEN
        redisCrudRepository.save(redisCrud);

        RedisCrud findCrud = redisCrudRepository.findById(id).get();

        // THEN
        Assertions.assertEquals("description", findCrud.getDescription());
        Assertions.assertEquals(updatedAt, findCrud.getUpdatedAt());
        log.info(findCrud.toString());
        
    }

    @Test
    @DisplayName("기본 등록 및 수정")
    public void update() {
        // GIVEN
        Long id = 0l;
        String description = "description";

        RedisCrud redisCrud = RedisCrud.builder()
                .id(id)
                .description(description)
                .updatedAt(updatedAt)
                .build();

        redisCrudRepository.save(redisCrud);
        log.info("[SAVE] Redis Crud = " + redisCrud);

        // WHEN
        RedisCrud redisCrudUpdate = redisCrudRepository.findById(id).get();
        redisCrudUpdate.update("Updated description", LocalDateTime.now());
        redisCrudRepository.save(redisCrudUpdate);

        // THEN
        RedisCrud findCrud = redisCrudRepository.findById(id).get();
        log.info("[UPDATE] Redis Crud = " + findCrud);
        Assertions.assertEquals("Updated description", findCrud.getDescription());

    }

}