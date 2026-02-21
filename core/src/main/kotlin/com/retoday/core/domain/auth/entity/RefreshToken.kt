package com.retoday.core.domain.auth.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash
class RefreshToken(
    @Id
    val userId: Long,
    val content: String,
    @TimeToLive
    val expiration: Long
)
