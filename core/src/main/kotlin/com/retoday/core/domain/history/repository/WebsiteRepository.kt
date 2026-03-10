package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.Website
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WebsiteRepository : ListCrudRepository<Website, Long> {
    fun findByDomain(domain: String): Website?
}
