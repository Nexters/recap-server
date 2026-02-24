package com.retoday.core.domain.user.repository

import com.retoday.core.domain.user.entity.UserExcludedWebsiteDomain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserExcludedWebsiteRepository : JpaRepository<UserExcludedWebsiteDomain, Long> {
    fun findAllByUserId(userId: Long): List<UserExcludedWebsiteDomain>

    fun existsByUserIdAndDomain(
        userId: Long,
        domain: String
    ): Boolean

    fun deleteByUserIdAndDomain(
        userId: Long,
        domain: String
    ): Long
}
