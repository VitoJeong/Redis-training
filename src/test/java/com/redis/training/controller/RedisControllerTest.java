package com.redis.training.controller;

import com.redis.training.controller.dto.RedisCrudSaveRequestDto;
import com.redis.training.domain.RedisCrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class RedisControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RedisCrudRepository redisCrudRepository;

    @AfterEach
    public void tearDown() throws Exception{
        redisCrudRepository.deleteAll();
    }

    @Test
    @DisplayName("기본 통신 테스트")
    void ok() {

        // GIVEN
        String url = "http://localhost:" + port;
        url = url.concat("/redis");
        url = url.concat("/");

        log.info(url);

        // WHEN
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // THEN
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals("ok", responseEntity.getBody());

    }

    @Test
    @DisplayName("등록 테스트")
    void save() {

        // GIVEN
        RedisCrudSaveRequestDto requestDto = RedisCrudSaveRequestDto.builder()
                .id(1l)
                .description("description")
                .updatedAt(LocalDateTime.now())
                .build();

        String url = "http://localhost:" + port;
        url = url.concat("/redis");
        url = url.concat("/cache");

        log.info(url);

        // WHEN
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // THEN
        log.info(responseEntity.toString());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertTrue(0L < responseEntity.getBody());
    }
}