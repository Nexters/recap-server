package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.dto.command.HistoryRecordCommand
import com.retoday.core.domain.history.dto.query.GetMyScreenTimesQuery
import com.retoday.core.domain.history.dto.result.GetMyScreenTimesResult
import com.retoday.core.domain.history.dto.result.HistoryRecordResult
import com.retoday.core.domain.history.entity.History
import com.retoday.core.domain.history.exception.DuplicateHistoryException
import com.retoday.core.domain.history.exception.InvalidTimeRangeException
import com.retoday.core.domain.history.repository.HistoryRepository
import com.retoday.core.domain.user.repository.ProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class HistoryService(
    private val historyRepository: HistoryRepository,
    private val profileRepository: ProfileRepository,
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

    @Transactional(readOnly = true)
    fun getMyScreenTimes(
        userId: Long,
        query: GetMyScreenTimesQuery
    ): GetMyScreenTimesResult =
        with(query) {
            val profile = profileRepository.findByUserId(userId)!!
            val periodStartedAt =
                period
                    .getStartedAt(date)
                    .atStartOfDay(profile.timeZone.id)
                    .toInstant()
            val periodEndedAt = periodStartedAt.plus(period.screenTimeDuration.inWholeDays, ChronoUnit.DAYS)
            val histories =
                historyRepository.findAllByUserIdAndVisitedAtBeforeAndClosedAtAfter(
                    userId = userId,
                    visitedAt = periodEndedAt,
                    closedAt = periodStartedAt
                )
            val stayDurations = MutableList(period.screenTimeCount) { 0L }
            var totalStayDuration = 0L

            histories.forEach {
                var startedAt = it.visitedAt.coerceIn(periodStartedAt, periodEndedAt)
                val endedAt = it.closedAt.coerceIn(periodStartedAt, periodEndedAt)

                while (startedAt < endedAt) {
                    val offsetSecond = startedAt.epochSecond - periodStartedAt.epochSecond
                    val screenTimeIndex = (offsetSecond / period.screenTimeUnit.inWholeSeconds).toInt()
                    val segmentEnd =
                        minOf(
                            endedAt,
                            periodStartedAt.plus(
                                (screenTimeIndex + 1) * period.screenTimeUnit.inWholeSeconds,
                                ChronoUnit.SECONDS
                            )
                        )
                    val stayDuration = segmentEnd.epochSecond - startedAt.epochSecond

                    stayDurations[screenTimeIndex] += stayDuration
                    totalStayDuration += stayDuration
                    startedAt = segmentEnd
                }
            }

            val screenTimes =
                stayDurations.mapIndexed { index, stayDuration ->
                    val startedAt =
                        periodStartedAt
                            .plus(index * period.screenTimeUnit.inWholeSeconds, ChronoUnit.SECONDS)
                            .atZone(profile.timeZone.id)
                            .toLocalDate()
                    val endedAt =
                        minOf(
                            periodEndedAt,
                            periodStartedAt.plus(
                                (index + 1) * period.screenTimeUnit.inWholeSeconds,
                                ChronoUnit.SECONDS
                            )
                        ).atZone(profile.timeZone.id)
                            .toLocalDate()

                    GetMyScreenTimesResult.ScreenTime(
                        startedAt = startedAt,
                        endedAt = endedAt,
                        stayDuration = stayDuration
                    )
                }

            GetMyScreenTimesResult(
                period = period,
                startedAt =
                    periodStartedAt
                        .atZone(profile.timeZone.id)
                        .toLocalDate(),
                endedAt =
                    periodEndedAt
                        .minus(1, ChronoUnit.DAYS)
                        .atZone(profile.timeZone.id)
                        .toLocalDate(),
                totalStayDuration = totalStayDuration,
                screenTimes = screenTimes
            )
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
