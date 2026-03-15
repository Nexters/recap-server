package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.Website
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query

interface CustomWebsiteRepository {
    @Modifying
    @Query(
        """
            INSERT INTO website (id, domain, favicon_url, created_at)
            VALUES (
                :#{#website.id},
                :#{#website.domain},
                :#{#website.faviconUrl},
                NOW()
            )
            ON DUPLICATE KEY UPDATE
                favicon_url = COALESCE(favicon_url, VALUES(favicon_url))
        """
    )
    fun upsertByDomain(website: Website): Int

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
