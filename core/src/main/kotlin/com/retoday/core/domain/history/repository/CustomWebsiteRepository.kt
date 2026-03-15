package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.Website
import org.springframework.data.jdbc.repository.query.Query

interface CustomWebsiteRepository {
    @Query(
        """
            SELECT *
            FROM website
            WHERE domain = :domain
            FOR SHARE
        """
    )
    fun getByDomainForShare(domain: String): Website
}
