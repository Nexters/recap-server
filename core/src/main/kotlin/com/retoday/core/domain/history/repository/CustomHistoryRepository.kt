package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.dto.projection.WebsiteStatProjection
import com.retoday.core.domain.history.dto.projection.WebsiteStatWithCategoryProjection
import com.retoday.core.domain.history.dto.projection.WebsiteStatWithVisitCountProjection
import com.retoday.core.domain.history.dto.projection.WorkPatternHourlyCountProjection
import com.retoday.core.domain.recap.dto.projection.UserActivityProjection
import com.retoday.core.domain.recap.dto.projection.UserTimelineProjection
import org.springframework.data.jdbc.repository.query.Query
import java.time.Instant

interface CustomHistoryRepository {
    @Query(
        """
            SELECT
                TIMESTAMPDIFF(HOUR, :startedAt, h.visited_at) AS hour,
                COUNT(*) AS count
            FROM history h
            WHERE h.user_id = :userId
              AND h.visited_at >= :startedAt
              AND h.visited_at < DATE_ADD(:startedAt, INTERVAL 1 DAY)
            GROUP BY hour
            ORDER BY hour
        """
    )
    fun findHourlyHistoryCountsByUserId(
        userId: Long,
        startedAt: Instant
    ): List<WorkPatternHourlyCountProjection>

    @Query(
        """
            SELECT
                w.domain AS domain,
                w.favicon_url AS favicon_url,
                SUM(
                    TIMESTAMPDIFF(
                        SECOND,
                        GREATEST(h.visited_at, :startedAt),
                        LEAST(h.closed_at, :endedAt)
                    )
                ) AS stay_duration
            FROM history h
            JOIN website w ON w.id = h.website_id
            WHERE h.user_id = :userId
              AND h.visited_at < :endedAt
              AND h.closed_at > :startedAt
            GROUP BY h.website_id
            ORDER BY stay_duration DESC
            LIMIT 1
        """
    )
    fun findTopWebsiteStatByUserId(
        userId: Long,
        startedAt: Instant,
        endedAt: Instant
    ): WebsiteStatProjection?

    @Query(
        """
            SELECT
                w.domain AS domain,
                w.favicon_url AS favicon_url,
                wc.name AS category_name,
                SUM(
                    TIMESTAMPDIFF(
                        SECOND,
                        GREATEST(h.visited_at, :startedAt),
                        LEAST(h.closed_at, :endedAt)
                    )
                ) AS stay_duration
            FROM history h
            JOIN website w ON w.id = h.website_id
            LEFT JOIN website_category wc ON wc.id = w.category_id
            WHERE h.user_id = :userId
              AND h.visited_at < :endedAt
              AND h.closed_at > :startedAt
            GROUP BY h.website_id
            ORDER BY stay_duration DESC
        """
    )
    fun findWebsiteStatsWithCategoryByUserId(
        userId: Long,
        startedAt: Instant,
        endedAt: Instant
    ): List<WebsiteStatWithCategoryProjection>

    @Query(
        """
            SELECT
                w.domain AS domain,
                w.favicon_url AS favicon_url,
                COUNT(*) AS visit_count,
                SUM(
                    TIMESTAMPDIFF(
                        SECOND,
                        GREATEST(h.visited_at, :startedAt),
                        LEAST(h.closed_at, :endedAt)
                    )
                ) AS stay_duration
            FROM history h
            JOIN website w ON w.id = h.website_id
            WHERE h.user_id = :userId
              AND h.visited_at < :endedAt
              AND h.closed_at > :startedAt
            GROUP BY h.website_id
            ORDER BY visit_count DESC, stay_duration DESC
            LIMIT :limit
        """
    )
    fun findWebsiteStatsWithVisitCountByUserId(
        userId: Long,
        startedAt: Instant,
        endedAt: Instant,
        limit: Int
    ): List<WebsiteStatWithVisitCountProjection>

    @Query(
        """
            SELECT
                p.title AS title,
                p.description AS description,
                w.domain AS domain,
                wc.name AS category_name,
                TIMESTAMPDIFF(MINUTE, h.visited_at, h.closed_at) AS stay_duration
            FROM history h
            JOIN page p ON p.id = h.page_id
            JOIN website w ON w.id = h.website_id
            LEFT JOIN website_category wc ON wc.id = w.category_id
            WHERE h.user_id = :userId
              AND h.visited_at >= :startedAt
              AND h.visited_at < :endedAt
        """
    )
    fun findUserActivitiesForRecap(
        userId: Long,
        startedAt: Instant,
        endedAt: Instant
    ): List<UserActivityProjection>

    @Query(
        """
            SELECT
                p.title AS title,
                p.description AS description,
                wc.name AS category_name,
                h.visited_at AS visited_at,
                h.closed_at AS closed_at
            FROM history h
            JOIN page p ON p.id = h.page_id
            JOIN website w ON w.id = h.website_id
            LEFT JOIN website_category wc ON wc.id = w.category_id
            WHERE h.user_id = :userId
              AND h.visited_at >= :startedAt
              AND h.visited_at < :endedAt
            ORDER BY h.visited_at
        """
    )
    fun findUserTimelinesForRecap(
        userId: Long,
        startedAt: Instant,
        endedAt: Instant
    ): List<UserTimelineProjection>
}
