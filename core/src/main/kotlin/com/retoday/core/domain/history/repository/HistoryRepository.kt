package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.dto.projection.WebsiteStatProjection
import com.retoday.core.domain.history.dto.projection.WebsiteStatWithCategoryProjection
import com.retoday.core.domain.history.dto.projection.WebsiteStatWithVisitCountProjection
import com.retoday.core.domain.history.dto.projection.WorkPatternHourlyCountProjection
import com.retoday.core.domain.history.entity.History
import com.retoday.core.domain.recap.dto.projection.UserActivityProjection
import com.retoday.core.domain.recap.dto.projection.UserTimelineProjection
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

    fun findFirstByUserIdAndVisitedAtGreaterThanEqualAndVisitedAtLessThanOrderByVisitedAtAsc(
        userId: Long,
        startedAt: Instant,
        endedAt: Instant
    ): History?

    fun findFirstByUserIdAndVisitedAtGreaterThanEqualAndVisitedAtLessThanOrderByClosedAtDesc(
        userId: Long,
        startedAt: Instant,
        endedAt: Instant
    ): History?

    @Query(
        """
            SELECT
                CAST(TIMESTAMPDIFF(HOUR, :startedAt, h.visited_at) AS SIGNED) AS hour,
                CAST(COUNT(*) AS SIGNED) AS count
            FROM history h
            WHERE h.user_id = :userId
              AND h.visited_at >= :startedAt
              AND h.visited_at < DATE_ADD(:startedAt, INTERVAL 1 DAY)
            GROUP BY hour
            ORDER BY hour
            """,
        nativeQuery = true
    )
    fun findHourlyHistoryCountsByUserId(
        @Param("userId")
        userId: Long,
        @Param("startedAt")
        startedAt: Instant
    ): List<WorkPatternHourlyCountProjection>

    @Query(
        """
            SELECT
                w.domain AS domain,
                w.favicon_url AS faviconUrl,
                CAST(
                    SUM(
                        TIMESTAMPDIFF(
                            SECOND,
                            GREATEST(h.visited_at, :startedAt),
                            LEAST(h.closed_at, :endedAt)
                        )
                    ) AS SIGNED
                ) AS stayDuration
            FROM history h
            JOIN website w ON w.id = h.website_id
            WHERE h.user_id = :userId
              AND h.visited_at BETWEEN DATE_SUB(:startedAt, INTERVAL 1 DAY) AND :endedAt
              AND h.closed_at BETWEEN :startedAt AND DATE_ADD(:endedAt, INTERVAL 1 DAY)
            GROUP BY h.website_id, w.domain, w.favicon_url
            ORDER BY stayDuration DESC
            LIMIT 1
        """,
        nativeQuery = true
    )
    fun findTopWebsiteStatByUserId(
        @Param("userId")
        userId: Long,
        @Param("startedAt")
        startedAt: Instant,
        @Param("endedAt")
        endedAt: Instant
    ): WebsiteStatProjection?

    @Query(
        """
            SELECT
                w.domain AS domain,
                w.favicon_url AS faviconUrl,
                wc.name AS categoryName,
                CAST(
                    SUM(
                        TIMESTAMPDIFF(
                            SECOND,
                            GREATEST(h.visited_at, :startedAt),
                            LEAST(h.closed_at, :endedAt)
                        )
                    ) AS SIGNED
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
    ): List<WebsiteStatWithCategoryProjection>

    @Query(
        """
            SELECT
                w.domain AS domain,
                w.favicon_url AS faviconUrl,
                CAST(COUNT(*) AS SIGNED) AS visitCount,
                CAST(
                    SUM(
                        TIMESTAMPDIFF(
                            SECOND,
                            GREATEST(h.visited_at, :startedAt),
                            LEAST(h.closed_at, :endedAt)
                        )
                    ) AS SIGNED
                ) AS stayDuration
            FROM history h
            JOIN website w ON w.id = h.website_id
            WHERE h.user_id = :userId
              AND h.visited_at BETWEEN DATE_SUB(:startedAt, INTERVAL 1 DAY) AND :endedAt
              AND h.closed_at BETWEEN :startedAt AND DATE_ADD(:endedAt, INTERVAL 1 DAY)
            GROUP BY h.website_id, w.domain, w.favicon_url
            ORDER BY visitCount DESC, stayDuration DESC
            LIMIT :limit
        """,
        nativeQuery = true
    )
    fun findWebsiteStatsWithVisitCountByUserId(
        @Param("userId")
        userId: Long,
        @Param("startedAt")
        startedAt: Instant,
        @Param("endedAt")
        endedAt: Instant,
        @Param("limit")
        limit: Int
    ): List<WebsiteStatWithVisitCountProjection>

    @Query(
        """
            SELECT
                p.title AS title,
                p.description AS description,
                w.domain AS domain,
                wc.name AS categoryName,
                CAST(TIMESTAMPDIFF(MINUTE, h.visited_at, h.closed_at) AS SIGNED) AS stayDuration
            FROM history h
            JOIN page p ON p.id = h.page_id
            JOIN website w ON w.id = h.website_id
            LEFT JOIN website_category wc ON wc.id = w.category_id
            WHERE h.user_id = :userId
              AND h.visited_at >= :startedAt
              AND h.visited_at < :endedAt
        """,
        nativeQuery = true
    )
    fun findUserActivitiesForRecap(
        @Param("userId")
        userId: Long,
        @Param("startedAt")
        startedAt: Instant,
        @Param("endedAt")
        endedAt: Instant
    ): List<UserActivityProjection>

    @Query(
        """
            SELECT
                p.title AS title,
                p.description AS description,
                wc.name AS categoryName,
                h.visited_at AS visitedAt,
                h.closed_at AS closedAt
            FROM history h
            JOIN page p ON p.id = h.page_id
            JOIN website w ON w.id = h.website_id
            LEFT JOIN website_category wc ON wc.id = w.category_id
            WHERE h.user_id = :userId
              AND h.visited_at >= :startedAt
              AND h.visited_at < :endedAt
            ORDER BY h.visited_at
        """,
        nativeQuery = true
    )
    fun findUserTimelinesForRecap(
        @Param("userId")
        userId: Long,
        @Param("startedAt")
        startedAt: Instant,
        @Param("endedAt")
        endedAt: Instant
    ): List<UserTimelineProjection>
}
