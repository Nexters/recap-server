package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.Website
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WebsiteRepository : JpaRepository<Website, Long> {
    fun findByDomain(domain: String): Website?
}
