package com.retoday.core.global.ratelimit

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RateLimiter(
    private val redisTemplate: StringRedisTemplate
) {
    companion object {
        private const val HISTORY_LIMIT = 10L
        private val HISTORY_WINDOW = Duration.ofMinutes(1)
    }

    private val script =
        RedisScript.of(
            """
            local count = redis.call('INCR', KEYS[1])
            if count == 1 then
                redis.call('EXPIRE', KEYS[1], ARGV[1])
            end
            return {count, redis.call('TTL', KEYS[1])}
            """.trimIndent(),
            List::class.java
        )

    fun checkHistoryExceeded(userId: Long): Long? =
        runCatching {
            val result =
                redisTemplate
                    .execute(script, listOf("rate:history:$userId"), HISTORY_WINDOW.seconds.toString())
                    .uncheckedCast<List<Long>>() ?: return null
            if (result[0] > HISTORY_LIMIT) result[1] else null
        }.getOrNull()

    @Suppress("UNCHECKED_CAST")
    private fun <T> Any?.uncheckedCast(): T? = this as? T
}
