package com.retoday.core.domain.user.repository

import com.retoday.core.domain.user.entity.UserExcludedWebsiteDomain
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserExcludedWebsiteRepository : ListCrudRepository<UserExcludedWebsiteDomain, Long> {
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
