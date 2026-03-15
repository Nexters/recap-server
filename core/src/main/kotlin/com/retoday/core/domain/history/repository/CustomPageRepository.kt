package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.Page
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query

interface CustomPageRepository {
    @Modifying
    @Query(
        """
            INSERT INTO page (
                id,
                website_id,
                url,
                title,
                description,
                created_at
            )
            VALUES (
                :#{#page.id},
                :#{#page.websiteId},
                :#{#page.url},
                :#{#page.title},
                :#{#page.description},
                NOW()
            )
            ON DUPLICATE KEY UPDATE
                title = COALESCE(title, VALUES(title)),
                description = COALESCE(description, VALUES(description))
        """
    )
    fun upsertByUrl(page: Page): Int

    @Query(
        """
            SELECT *
            FROM page
            WHERE url = :url
            FOR SHARE
        """
    )
    fun getByUrlForShare(url: String): Page
}
