package com.retoday.core.domain.recap.service

import com.retoday.core.domain.history.dto.query.GetMyCategoryAnalysisQuery
import com.retoday.core.domain.history.repository.HistoryRepository
import com.retoday.core.domain.history.service.HistoryService
import com.retoday.core.domain.recap.client.RecapAIClient
import com.retoday.core.domain.recap.component.ImagePolicyResolver
import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.projection.UserActivityProjection
import com.retoday.core.domain.recap.dto.projection.UserTimelineProjection
import com.retoday.core.domain.recap.dto.request.GenerateRecapRequest
import com.retoday.core.domain.recap.dto.request.RecapPayload
import com.retoday.core.domain.recap.dto.request.UserActivityRequest
import com.retoday.core.domain.recap.dto.request.UserTimelineRequest
import com.retoday.core.domain.recap.dto.response.GeminiRecapResponse
import com.retoday.core.domain.recap.dto.response.GeminiTimelineResponse
import com.retoday.core.domain.recap.dto.response.GeminiTopicResponse
import com.retoday.core.domain.recap.dto.response.RecapDetailResponse
import com.retoday.core.domain.recap.entity.Recap
import com.retoday.core.domain.recap.entity.RecapStatus
import com.retoday.core.domain.recap.entity.Section
import com.retoday.core.domain.recap.entity.Timeline
import com.retoday.core.domain.recap.entity.Topic
import com.retoday.core.domain.recap.repository.RecapRepository
import com.retoday.core.domain.recap.repository.SectionRepository
import com.retoday.core.domain.recap.repository.TimelineRepository
import com.retoday.core.domain.recap.repository.TopicRepository
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.global.extension.transaction
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class RecapService(
    private val recapAIClient: RecapAIClient,
    private val recapRepository: RecapRepository,
    private val sectionRepository: SectionRepository,
    private val topicRepository: TopicRepository,
    private val timelineRepository: TimelineRepository,
    private val historyRepository: HistoryRepository,
    private val profileRepository: ProfileRepository,
    private val transactionManager: PlatformTransactionManager,
    private val historyService: HistoryService,
    private val imagePolicyResolver: ImagePolicyResolver
) {
    private companion object {
        val TIMELINE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")
        const val FAILED_RECAP_TITLE: String = ""
        const val FAILED_RECAP_SUMMARY: String = ""
    }

    fun generateDailyRecap(
        userId: Long,
        date: LocalDate?
    ): RecapDetailResponse? {
        val targetDate = date ?: LocalDate.now().minusDays(1)
        createDailyRecap(userId, targetDate)

        return getRecapDetail(userId, targetDate)
    }

    fun getDailyRecap(
        userId: Long,
        date: LocalDate?
    ): RecapDetailResponse? {
        val targetDate = date ?: LocalDate.now().minusDays(1)
        return getRecapDetail(userId, targetDate)
    }

    // 리캡 생성 로직
    fun createDailyRecap(
        userId: Long,
        date: LocalDate
    ) {
        val profile = profileRepository.findByUserId(userId)!!
        val startedAt =
            date
                .atStartOfDay(profile.timeZone.id)
                .toInstant()
        val endedAt = startedAt.plus(Duration.ofDays(1))

        if (recapRepository.existsByUserIdAndRecapDate(userId, date)) return
        val activityProjections = historyRepository.findUserActivitiesForRecap(userId, startedAt, endedAt)
        if (activityProjections.isEmpty()) return
        val activityRequests = activityProjections.map { it.toRequest() }
        val name = profile.firstName
        val zoneId = profile.timeZone.id

        val timelineProjections = historyRepository.findUserTimelinesForRecap(userId, startedAt, endedAt)
        val firstVisitedAt = timelineProjections.mapNotNull { it.visitedAt }.minOrNull() ?: startedAt
        val lastClosedAt = timelineProjections.mapNotNull { it.closedAt }.maxOrNull() ?: endedAt

        runCatching {
            val recapResponse = generateRecap(name, activityRequests)
            val topicResponse = generateTopics(name, activityRequests)

            var timelineResponse = GeminiTimelineResponse()
            if (timelineProjections.isNotEmpty()) {
                val timelineRequests = timelineProjections.map { it.toRequest() }
                timelineResponse = generateTimeline(name, timelineRequests)
            }

            val categoryAnalyses =
                historyService
                    .getMyCategoryAnalyses(
                        userId = userId,
                        query = GetMyCategoryAnalysisQuery(date = date)
                    ).categoryAnalyses
            val topCategoryName = categoryAnalyses.firstOrNull()?.categoryName
            val categoryCount = categoryAnalyses.count { it.stayDuration > 0L }

            val imageUrl =
                imagePolicyResolver.resolveImageUrl(
                    userId = userId,
                    firstVisitedAt = firstVisitedAt,
                    zoneId = zoneId,
                    topCategoryName = topCategoryName,
                    categoryCount = categoryCount,
                    activities = activityProjections
                )

            transactionManager.transaction {
                val recap =
                    Recap(
                        userId = userId,
                        recapDate = date,
                        title = recapResponse.title,
                        summary = recapResponse.dailySummary,
                        imageUrl = imageUrl,
                        startedAt = firstVisitedAt,
                        closedAt = lastClosedAt,
                        model = recapAIClient.modelName,
                        status = RecapStatus.COMPLETED
                    ).let { recapRepository.save(it) }

                val sections =
                    recapResponse.sections
                        .map {
                            Section(
                                recapId = recap.id!!,
                                title = it.title,
                                content = it.content
                            )
                        }.let { sectionRepository.saveAll(it) }

                val topics =
                    topicResponse.topics
                        .map {
                            Topic(
                                recapId = recap.id!!,
                                keyword = it.keyword,
                                title = it.title,
                                content = it.content
                            )
                        }.let { topicRepository.saveAll(it) }

                if (timelineResponse.timelines.isEmpty()) return@transaction

                val timelines =
                    timelineResponse.timelines
                        .map { item ->
                            val startedAt = LocalTime.parse(item.startedAt, TIMELINE_TIME_FORMATTER)
                            val endedAt = LocalTime.parse(item.endedAt, TIMELINE_TIME_FORMATTER)
                            val duration =
                                Duration
                                    .between(startedAt, endedAt)
                                    .toMinutes()
                                    .toInt()

                            Timeline(
                                recapId = recap.id!!,
                                startedAt = startedAt,
                                endedAt = endedAt,
                                title = item.title,
                                durationMinutes = duration
                            )
                        }.let { timelineRepository.saveAll(it) }
            }
        }.onFailure {
            transactionManager.transaction {
                recapRepository.save(
                    Recap(
                        userId = userId,
                        recapDate = date,
                        title = FAILED_RECAP_TITLE,
                        summary = FAILED_RECAP_SUMMARY,
                        startedAt = firstVisitedAt,
                        closedAt = lastClosedAt,
                        model = recapAIClient.modelName,
                        status = RecapStatus.FAILED
                    )
                )
            }
        }.getOrThrow()
    }

    // api 호출용 조회 로직
    fun getRecapDetail(
        userId: Long,
        date: LocalDate
    ): RecapDetailResponse? =
        transactionManager.transaction(readOnly = true) {
            recapRepository
                .findByUserIdAndRecapDate(userId, date)
                ?.let {
                    val sections = sectionRepository.findAllByRecapId(it.id!!)
                    val topics = topicRepository.findAllByRecapId(it.id!!)
                    val timelines = timelineRepository.findAllByRecapId(it.id!!)
                    val zoneId = profileRepository.findByUserId(userId)!!.timeZone.id

                    RecapDetailResponse.of(it, sections, timelines, topics, zoneId)
                }
        }

    // AI Generation Methods
    fun generateRecap(
        name: String,
        activities: List<UserActivityRequest>
    ) = recapAIClient.generate(
        GenerateRecapRequest(
            type = RecapType.TODAY_RECAP,
            nickname = name,
            payload = RecapPayload.Activities(activities)
        ),
        GeminiRecapResponse::class.java
    )

    fun generateTimeline(
        name: String,
        activities: List<UserTimelineRequest>
    ) = recapAIClient.generate(
        GenerateRecapRequest(
            type = RecapType.TIMELINE,
            nickname = name,
            payload = RecapPayload.Timelines(activities)
        ),
        GeminiTimelineResponse::class.java
    )

    fun generateTopics(
        name: String,
        activities: List<UserActivityRequest>
    ) = recapAIClient.generate(
        GenerateRecapRequest(
            type = RecapType.TOPIC,
            nickname = name,
            payload = RecapPayload.Activities(activities)
        ),
        GeminiTopicResponse::class.java
    )

    private fun UserActivityProjection.toRequest(): UserActivityRequest =
        UserActivityRequest(
            title = title,
            description = description,
            domain = domain,
            categoryName = categoryName,
            stayDuration = stayDuration.toInt()
        )

    private fun UserTimelineProjection.toRequest(): UserTimelineRequest =
        UserTimelineRequest(
            title = title,
            description = description,
            categoryName = categoryName,
            visitedAt = visitedAt,
            closedAt = closedAt
        )
}
