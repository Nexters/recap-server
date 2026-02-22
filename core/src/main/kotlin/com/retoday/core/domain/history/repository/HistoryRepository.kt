package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.dto.projection.WebsiteStat
import com.retoday.core.domain.history.dto.projection.WebsiteStatWithCategory
import com.retoday.core.domain.history.dto.projection.WebsiteStatWithVisitCount
import com.retoday.core.domain.history.dto.projection.WorkPatternHourlyCount
import com.retoday.core.domain.history.entity.History
import com.retoday.core.domain.recap.dto.projection.UserActivityProjection
import com.retoday.core.domain.recap.dto.projection.UserTimelineProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.time.LocalDate

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
    ): List<WorkPatternHourlyCount>

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
    ): WebsiteStat?

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
    ): List<WebsiteStatWithCategory>

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
    ): List<WebsiteStatWithVisitCount>

    // UserActivity
    @Query(
        """
        SELECT new com.retoday.core.domain.recap.dto.projection.UserActivityProjection(
            p.title,
            p.description,
            w.domain,
            c.name,
            h.stayDuration
        )
        FROM History h
        JOIN Page p ON h.pageId = p.id
        JOIN Website w ON h.websiteId = w.id
        LEFT JOIN WebsiteCategory c ON w.categoryId = c.id
        WHERE h.userId = :userId
          AND h.visitedDate = :date
    """
    )
    fun findUserActivitiesForRecap(
        @Param("userId") userId: Long,
        @Param("date") date: LocalDate
    ): List<UserActivityProjection>

    // UserTimeline
    @Query(
        """
        SELECT new com.retoday.core.domain.recap.dto.projection.UserTimelineProjection(
            p.title,
            p.description,
            c.name,
            h.visitedAt,
            h.closedAt
        )
        FROM History h
        JOIN Page p ON h.pageId = p.id
        JOIN Website w ON h.websiteId = w.id
        LEFT JOIN WebsiteCategory c ON w.categoryId = c.id
        WHERE h.userId = :userId
          AND h.visitedDate = :date
        ORDER BY h.visitedAt ASC
    """
    )
    fun findUserTimelinesForRecap(
        @Param("userId") userId: Long,
        @Param("date") date: LocalDate
    ): List<UserTimelineProjection>

    // (임시) Recap의 startAt, closeAt을 채우기 위해 해당 날짜의 첫 방문과 마지막 종료 시간 조회
    // 전날부터 이어지는 활동에 대한 조건 추가 예정
    fun findFirstByUserIdAndVisitedDateOrderByVisitedAtAsc(
        userId: Long,
        date: LocalDate
    ): History?

    fun findFirstByUserIdAndVisitedDateOrderByClosedAtDesc(
        userId: Long,
        date: LocalDate
    ): History?
}
