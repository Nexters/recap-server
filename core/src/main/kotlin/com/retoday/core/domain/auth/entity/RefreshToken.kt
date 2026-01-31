package com.retoday.core.domain.auth.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.time.Duration

@RedisHash
class RefreshToken(
    @Id
    val userId: Long,
    val content: String,
    expiration: Duration
) {
    @TimeToLive
    private val ttl = expiration.toSeconds()
}
