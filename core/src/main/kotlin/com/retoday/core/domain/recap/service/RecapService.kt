package com.retoday.core.domain.recap.service

import com.retoday.core.domain.history.repository.HistoryRepository
import com.retoday.core.domain.recap.client.RecapAIClient
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
import com.retoday.core.domain.recap.entity.Section
import com.retoday.core.domain.recap.entity.Timeline
import com.retoday.core.domain.recap.entity.Topic
import com.retoday.core.domain.recap.repository.RecapRepository
import com.retoday.core.domain.recap.repository.SectionRepository
import com.retoday.core.domain.recap.repository.TimelineRepository
import com.retoday.core.domain.recap.repository.TopicRepository
import com.retoday.core.domain.user.repository.ProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
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
    private val profileRepository: ProfileRepository
) {
    private companion object {
        val TIMELINE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")
    }

    @Transactional
    fun generateDailyRecap(
        userId: Long,
        date: LocalDate
    ): RecapDetailResponse? {
        createDailyRecap(userId, date)
        return getRecapDetail(userId, date)
    }

    // 리캡 생성 로직
    @Transactional
    fun createDailyRecap(
        userId: Long,
        date: LocalDate
    ) {
        if (recapRepository.existsByUserIdAndRecapDate(userId, date)) return
        val activityProjections = historyRepository.findUserActivitiesForRecap(userId, date)
        if (activityProjections.isEmpty()) return
        val activityRequests = activityProjections.map { it.toRequest() }
        val profile = profileRepository.findByUserId(userId)!!
        val name = profile.firstName

        val firstHistory = historyRepository.findFirstByUserIdAndVisitedDateOrderByVisitedAtAsc(userId, date)
        val lastHistory = historyRepository.findFirstByUserIdAndVisitedDateOrderByClosedAtDesc(userId, date)

        val response = generateRecap(name, activityRequests)
        val recap =
            Recap(
                userId = userId,
                recapDate = date,
                title = response.title,
                summary = response.dailySummary,
                startedAt = firstHistory?.visitedAt ?: Instant.now(),
                closedAt = lastHistory?.closedAt ?: Instant.now(),
                model = recapAIClient.modelName
            ).let { recapRepository.save(it) }

        val sections =
            response.sections.map {
                Section(recapId = recap.id!!, title = it.title, content = it.content)
            }
        sectionRepository.saveAll(sections)

        saveTopicsInternal(recap, name, activityRequests)

        // 타임라인 전용 데이터 조회 및 저장
        val timelineProjections = historyRepository.findUserTimelinesForRecap(userId, date)
        if (timelineProjections.isNotEmpty()) {
            val timelineRequests = timelineProjections.map { it.toRequest() }
            saveTimelinesInternal(recap, name, timelineRequests)
        }
    }

    // api 호출용 조회 로직
    @Transactional(readOnly = true)
    fun getRecapDetail(
        userId: Long,
        date: LocalDate
    ): RecapDetailResponse? {
        val recap = recapRepository.findByUserIdAndRecapDate(userId, date) ?: return null

        val sections = sectionRepository.findAllByRecapId(recap.id!!)
        val topics = topicRepository.findAllByRecapId(recap.id!!)
        val timelines = timelineRepository.findAllByRecapId(recap.id!!)

        return RecapDetailResponse.of(recap, sections, timelines, topics)
    }

    private fun saveTimelinesInternal(
        recap: Recap,
        name: String,
        activities: List<UserTimelineRequest>
    ) {
        // 2. 가공된 데이터(enrichedActivities)를 AI에게 전달
        val timelineResponse = generateTimeline(name, activities)

        val timelines =
            timelineResponse.timelines.map { it ->
                val startedAt = LocalTime.parse(it.startedAt, TIMELINE_TIME_FORMATTER)
                val endedAt = LocalTime.parse(it.endedAt, TIMELINE_TIME_FORMATTER)
                val duration = calculateDuration(startedAt, endedAt)

                // 2. 엔티티 필드명에 정확히 매핑
                Timeline(
                    recapId = recap.id!!, // 필수 필드
                    startedAt = startedAt,
                    endedAt = endedAt,
                    title = it.title,
                    durationMinutes = duration
                )
            }
        timelineRepository.saveAll(timelines)
    }

    private fun saveTopicsInternal(
        recap: Recap,
        name: String,
        activities: List<UserActivityRequest>
    ) {
        val topicResponse = generateTopics(name, activities)
        val topics =
            topicResponse.topics.map {
                Topic(
                    recapId = recap.id!!,
                    keyword = it.keyword,
                    title = it.title,
                    content = it.content
                )
            }
        topicRepository.saveAll(topics)
    }

    // 시간 계산 헬퍼 함수
    private fun calculateDuration(
        startedAt: LocalTime,
        endedAt: LocalTime
    ): Int = endedAt.toSecondOfDay() / 60 - startedAt.toSecondOfDay() / 60

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
            stayDuration = stayDuration
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
