package com.redis.training.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
@DisplayName("Redis 데이터 타입 테스트")
class RedisConfigTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    @DisplayName("String 테스트")
    public void testString() {

        final String key = "test_String";
        final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        valueOperations.set(key, "1");
        final String resultA = valueOperations.get(key);

        log.info("resultA = " + resultA);

        valueOperations.increment(key); // value의 값을 증가
        final String resultB = valueOperations.get(key);

        log.info("resultB = " + resultB);

    }

    @Test
    @DisplayName("List 테스트")
    public void testList() {
        final String key = "test_List";
        final ListOperations<String, String> listOperations = redisTemplate.opsForList();

        listOperations.rightPush(key, "H");
        listOperations.rightPush(key, "E");
        listOperations.rightPush(key, "L");
        listOperations.rightPush(key, "L");
        listOperations.rightPush(key, "O");

        listOperations.rightPushAll(key, "", "W", "O", "L", "R", "D");

        final String characterOne = listOperations.index(key, 1);
        log.info("characterOne = " + characterOne);

        final Long size = listOperations.size(key);
        log.info("size = " + size);

        final List<String> resultRange = listOperations.range(key, 6, 10);

        assert resultRange != null;
        log.info("resultRange = " + Arrays.toString(resultRange.toArray()));

    }

    @Test
    @DisplayName("Set 테스트")
    public void testSet() {
        final String key = "test_Set";
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();

        setOperations.add(key, "H");
        setOperations.add(key, "e");
        setOperations.add(key, "l");
        setOperations.add(key, "l");
        setOperations.add(key, "o");

        Set<String> result = setOperations.members(key);

        assert result != null;
        log.info("result = " + Arrays.toString(result.toArray()));

        final Long size = setOperations.size(key);
        log.info("size = " + size);

        Cursor<String> cursor = setOperations
                .scan(key, ScanOptions.scanOptions().match("*").count(3).build());

        while (cursor.hasNext()) {
            log.info("cursor = " + cursor.next());
        }

    }

    @Test
    @DisplayName("Sorted Set 테스트")
    public void testSortedSet() {
        final String key = "test_Sorted_Set";
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();

        zSetOperations.add(key, "H", 1);
        zSetOperations.add(key, "E", 5);
        zSetOperations.add(key, "L", 10);
        zSetOperations.add(key, "L", 15);
        zSetOperations.add(key, "O", 20);

        Set<String> range = zSetOperations.range(key, 0, 5);

        assert range != null;
        log.info("range = " + Arrays.toString(range.toArray()));

        final Long size = zSetOperations.size(key);
        log.info("size = " + size);

        Set<String> rangeByScore = zSetOperations.rangeByScore(key, 0, 13);
        assert rangeByScore != null;
        log.info("rangeByScore = " + Arrays.toString(rangeByScore.toArray()));

    }

    @Test
    @DisplayName("Hash 테스트")
    public void testHash() {
        final String key = "test_Hash";
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        hashOperations.put(key, "Hello 1", "test Hash 1");
        hashOperations.put(key, "Hello 2", "Second hash");
        hashOperations.put(key, "Hello 3", "TEST Hash 3");

        Object hello = hashOperations.get(key, "Hello 2");
        log.info("hello = " + hello);

        Map<Object, Object> entries = hashOperations.entries(key);
        log.info("entries = " + entries.get("Hello 3"));

        final Long size = hashOperations.size(key);
        log.info("size = " + size);

    }

}