package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.dto.projection.WebsiteStatWithCategory
import com.retoday.core.domain.history.entity.History
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant

interface HistoryRepository : JpaRepository<History, Long> {
    fun findByUserIdAndPageIdAndVisitedAtAfter(
        userId: Long,
        pageId: Long,
        visitedAt: Instant
    ): History?

    fun findAllByUserIdAndVisitedAtBeforeAndClosedAtAfter(
        userId: Long,
        visitedAt: Instant,
        closedAt: Instant
    ): List<History>

    @Query(
        """
            SELECT
                w.domain AS domain,
                w.favicon_url AS faviconUrl,
                wc.name AS categoryName,
                SUM(
                    TIMESTAMPDIFF(
                        SECOND,
                        GREATEST(h.visited_at, :startedAt),
                        LEAST(h.closed_at, :endedAt)
                    )
                ) AS stayDuration
            FROM history h
            JOIN website w ON w.id = h.website_id
            LEFT JOIN website_category wc ON wc.id = w.category_id
            WHERE h.user_id = :userId
              AND h.visited_at BETWEEN DATE_SUB(:startedAt, INTERVAL 1 DAY) AND :endedAt
              AND h.closed_at BETWEEN :startedAt AND DATE_ADD(:endedAt, INTERVAL 1 DAY)
            GROUP BY h.website_id, w.domain, w.favicon_url, wc.name
            ORDER BY stayDuration DESC
            """,
        nativeQuery = true
    )
    fun findWebsiteStatsWithCategoryByUserId(
        @Param("userId")
        userId: Long,
        @Param("startedAt")
        startedAt: Instant,
        @Param("endedAt")
        endedAt: Instant
    ): List<WebsiteStatWithCategory>
}
