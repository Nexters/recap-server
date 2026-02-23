package com.retoday.core.domain.user.service

import com.retoday.core.domain.user.repository.UserExcludedWebsiteRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class ExcludedDomainCacheService(
    private val userExcludedWebsiteRepository: UserExcludedWebsiteRepository,
    private val redisTemplate: StringRedisTemplate
) {
    private fun cacheKey(userId: Long) = "excluded-domains:$userId"

    fun isExcluded(
        userId: Long,
        domain: String
    ): Boolean {
        val excludedDomains = getExcludedDomains(userId)
        return excludedDomains.any { excluded ->
            domain == excluded || domain.endsWith(".$excluded")
        }
    }

    fun invalidate(userId: Long) {
        redisTemplate.delete(cacheKey(userId))
    }

    private fun getExcludedDomains(userId: Long): List<String> {
        val key = cacheKey(userId)

        redisTemplate.opsForValue().get(key)?.let { cached ->
            return if (cached.isEmpty()) emptyList() else cached.split(",")
        }

        return userExcludedWebsiteRepository
            .findAllByUserId(userId)
            .map { it.domain }
            .also { domains ->
                // 도메인이 없는 경우 빈 문자열로 캐싱하여 매번 DB 조회 방지
                redisTemplate.opsForValue().set(key, domains.joinToString(","), Duration.ofHours(1))
            }
    }
}
