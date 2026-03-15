package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.Page
import org.springframework.data.jdbc.repository.query.Query

interface CustomPageRepository {
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
