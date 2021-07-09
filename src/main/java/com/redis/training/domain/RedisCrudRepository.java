package com.redis.training.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RedisCrudRepository extends JpaRepository<RedisCrud, Long> {
}
