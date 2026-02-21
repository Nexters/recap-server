package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.client.AICategoryClient
import com.retoday.core.domain.history.dto.command.HistoryRecordCommand
import com.retoday.core.domain.history.dto.query.GetMyScreenTimesQuery
import com.retoday.core.domain.history.dto.result.GetMyScreenTimesResult
import com.retoday.core.domain.history.dto.result.HistoryRecordResult
import com.retoday.core.domain.history.entity.History
import com.retoday.core.domain.history.entity.Website
import com.retoday.core.domain.history.exception.DuplicateHistoryException
import com.retoday.core.domain.history.exception.InvalidCategoryException
import com.retoday.core.domain.history.exception.InvalidTimeRangeException
import com.retoday.core.domain.history.repository.HistoryRepository
import com.retoday.core.domain.history.repository.WebsiteCategoryRepository
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
    private val pageService: PageService,
    private val categoryRepository: WebsiteCategoryRepository,
    private val aiClient: AICategoryClient
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

            // period에 맞춰 집계 시작 시각을 계산
            val periodStartedAt =
                period
                    .getStartedAt(date)
                    .atStartOfDay(profile.timeZone.id)
                    .toInstant()

            // 집계 종료 시각은 시작 시각 + period 길이(1일/7일)로 계산
            // 이하 집계는 [periodStartedAt, periodEndedAt) 구간 내에서 처리
            val periodEndedAt = periodStartedAt.plus(period.screenTimeDuration.inWholeDays, ChronoUnit.DAYS)

            // 집계 구간과 겹치는 History([visitedAt, closedAt)와 집계 구간이 교집합을 가지는 데이터)들을 조회
            val histories =
                historyRepository.findAllByUserIdAndVisitedAtBeforeAndClosedAtAfter(
                    userId = userId,
                    visitedAt = periodEndedAt,
                    closedAt = periodStartedAt
                )

            // 집계 구간을 period.screenTimeUnit 단위의 스크린타임들로 분할
            // ex. DAILY: 2시간 단위 12개, WEEKLY: 하루 단위 7개.
            val stayDurations = MutableList(period.screenTimeCount) { 0L }
            var totalStayDuration = 0L

            histories.forEach {
                // 개별 History가 집계 구간 밖으로 벗어날 수 있으므로 전처리
                var startedAt = it.visitedAt.coerceIn(periodStartedAt, periodEndedAt)
                val endedAt = it.closedAt.coerceIn(periodStartedAt, periodEndedAt)

                // 하나의 History가 여러 스크린타임에 포함될 수 있으므로, period.screenTimeUnit 경계마다 분할하여 각 스크린타임에 체류시간을 배분
                while (startedAt < endedAt) {
                    // startedAt이 집계 시작으로부터 몇 초 떨어져 있는지 오프셋 계산
                    val offsetSecond = startedAt.epochSecond - periodStartedAt.epochSecond

                    // 오프셋 기반으로 어떤 스크린타임에 포함될지 인덱스 계산
                    val screenTimeIndex = (offsetSecond / period.screenTimeUnit.inWholeSeconds).toInt()

                    // 현재 스크린타임의 끝과 History 종료 시각 중 더 이른 시각까지를 이번 분할 구간으로 계산
                    val segmentEnd =
                        minOf(
                            endedAt,
                            periodStartedAt.plus(
                                (screenTimeIndex + 1) * period.screenTimeUnit.inWholeSeconds,
                                ChronoUnit.SECONDS
                            )
                        )

                    // 현재 스크린타임에 포함될 체류시간 계산
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
                            .toLocalDateTime()
                    val endedAt =
                        minOf(
                            periodEndedAt,
                            periodStartedAt.plus(
                                (index + 1) * period.screenTimeUnit.inWholeSeconds,
                                ChronoUnit.SECONDS
                            )
                        ).atZone(profile.timeZone.id)
                            .toLocalDateTime()

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

    @Transactional
    fun classifyCategory(
        website: Website,
        domain: String
    ) {
        val categories =
            categoryRepository
                .findAll()
                .map { it.name }

        val predictedName =
            aiClient.classify(domain, categories)

        val category =
            categoryRepository
                .findByName(predictedName)
                ?: throw InvalidCategoryException()

        website.updateCategory(category.id!!)
    }
}
