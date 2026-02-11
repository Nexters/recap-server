package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.History
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface HistoryRepository : JpaRepository<History, Long> {
    fun findByUserIdAndPageIdAndVisitedAtAfter(
        userId: Long,
        pageId: Long,
        visitedAt: Instant
    ): History?
}
