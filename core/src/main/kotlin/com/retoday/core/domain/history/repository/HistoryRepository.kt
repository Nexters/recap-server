package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.History
import org.springframework.data.repository.ListCrudRepository
import java.time.Instant

interface HistoryRepository : ListCrudRepository<History, Long>, CustomHistoryRepository {
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
}
