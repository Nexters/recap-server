package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.History
import com.retoday.core.domain.recap.dto.UserActivityDto
import com.retoday.core.domain.recap.dto.UserTimelineDto
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
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

    // UserActivity
    @Query(
        """
        SELECT new com.retoday.core.domain.recap.dto.UserActivityDto(
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
    ): List<UserActivityDto>

    // UserTimeline
    @Query(
        """
        SELECT new com.retoday.core.domain.recap.dto.UserTimelineDto(
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
    ): List<UserTimelineDto>

    // (임시) RecapEntity의 startAt, closeAt을 채우기 위해 해당 날짜의 첫 방문과 마지막 종료 시간 조회
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
