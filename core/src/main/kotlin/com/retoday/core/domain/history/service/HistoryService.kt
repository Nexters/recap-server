package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.dto.command.HistoryRecordCommand
import com.retoday.core.domain.history.dto.result.HistoryRecordResult
import com.retoday.core.domain.history.entity.History
import com.retoday.core.domain.history.exception.DuplicateHistoryException
import com.retoday.core.domain.history.exception.InvalidTimeRangeException
import com.retoday.core.domain.history.repository.HistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class HistoryService(
    private val historyRepository: HistoryRepository,
    private val websiteService: WebsiteService,
    private val pageService: PageService
) {
    @Transactional
    fun recordHistory(
        userId: Long,
        command: HistoryRecordCommand
    ): HistoryRecordResult {
        require(command.closedAt.isAfter(command.visitedAt)) {
            throw InvalidTimeRangeException("closedAt은 visitedAt보다 이후여야 합니다")
        }

        val website = websiteService.findOrCreate(command.domain, command.faviconUrl)
        val page =
            pageService.findOrCreate(
                websiteId = website.id!!,
                url = command.normalizedUrl,
                title = command.title,
                description = command.description
            )

        checkDuplicateHistory(userId, page.id!!, command.visitedAt, command.tabId, command.normalizedUrl)

        return historyRepository
            .save(createHistory(userId, website.id!!, page.id!!, command))
            .let {
                HistoryRecordResult(
                    historyId = it.id!!,
                    pageId = page.id!!,
                    websiteId = website.id!!,
                    stayDuration = it.stayDuration,
                    recordedAt = it.createdAt
                )
            }
    }

    private fun checkDuplicateHistory(
        userId: Long,
        pageId: Long,
        visitedAt: Instant,
        tabId: Int,
        url: String
    ) {
        historyRepository
            .findByUserIdAndPageIdAndVisitedAtAfter(
                userId = userId,
                pageId = pageId,
                visitedAt = visitedAt.minusSeconds(10)
            )?.let { throw DuplicateHistoryException(tabId, url) }
    }

    private fun createHistory(
        userId: Long,
        websiteId: Long,
        pageId: Long,
        command: HistoryRecordCommand
    ) = History(
        userId = userId,
        websiteId = websiteId,
        pageId = pageId,
        visitedAt = command.visitedAt,
        closedAt = command.closedAt,
        stayDuration = command.stayDuration,
        visitedDate = command.visitedDate,
        visitedHour = command.visitedHour,
        isClosed = command.isClosed,
        scrollDepth = command.scrollDepth
    )
}
