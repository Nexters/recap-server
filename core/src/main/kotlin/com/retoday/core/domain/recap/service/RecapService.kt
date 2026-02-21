package com.retoday.core.domain.recap.service

import com.retoday.core.domain.history.repository.HistoryRepository
import com.retoday.core.domain.recap.client.RecapAIClient
import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.*
import com.retoday.core.domain.recap.entity.RecapEntity
import com.retoday.core.domain.recap.entity.SectionEntity
import com.retoday.core.domain.recap.entity.TimelineEntity
import com.retoday.core.domain.recap.entity.TopicEntity
import com.retoday.core.domain.recap.repository.RecapRepository
import com.retoday.core.domain.recap.repository.SectionRepository
import com.retoday.core.domain.recap.repository.TimelineRepository
import com.retoday.core.domain.recap.repository.TopicRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class RecapService(
    private val recapAIClient: RecapAIClient,
    private val recapRepository: RecapRepository,
    private val sectionRepository: SectionRepository,
    private val topicRepository: TopicRepository,
    private val timelineRepository: TimelineRepository,
    private val historyRepository: HistoryRepository
) {
    @Transactional
    fun createDailyRecap(
        userId: Long,
        name: String,
        date: LocalDate
    ) {
        val activities = historyRepository.findUserActivitiesForRecap(userId, date)
        if (activities.isEmpty()) return

        val firstHistory = historyRepository.findFirstByUserIdAndVisitedDateOrderByVisitedAtAsc(userId, date)
        val lastHistory = historyRepository.findFirstByUserIdAndVisitedDateOrderByClosedAtDesc(userId, date)

        val response = generateRecap(name, activities)
        val recap =
            RecapEntity(
                userId = userId,
                recapDate = date,
                title = response.title,
                summary = response.dailySummary,
                startAt =
                    firstHistory?.visitedAt?.let { LocalDateTime.ofInstant(it, ZoneId.systemDefault()) }
                        ?: LocalDateTime.now(),
                closeAt =
                    lastHistory?.closedAt?.let { LocalDateTime.ofInstant(it, ZoneId.systemDefault()) }
                        ?: LocalDateTime.now(),
                model = recapAIClient.modelName,
                createdAt = Instant.now()
            ).let { recapRepository.save(it) }

        val sections =
            response.sections.map {
                SectionEntity(recapId = recap.id!!, title = it.title, content = it.content)
            }
        sectionRepository.saveAll(sections)

        saveTopicsInternal(recap, name, activities)

        // 타임라인 전용 데이터 조회 및 저장
        val timelineActivities = historyRepository.findUserTimelinesForRecap(userId, date)
        if (timelineActivities.isNotEmpty()) {
            saveTimelinesInternal(recap, name, timelineActivities)
        }
    }

    private fun saveTimelinesInternal(
        recap: RecapEntity,
        name: String,
        activities: List<UserTimelineDto>
    ) {
        // generateTimeline은 이제 List<UserTimelineDto>를 명확히 받습니다.
        val timelineResponse = generateTimeline(name, activities)
        val timelines =
            timelineResponse.timelines.map {
                TimelineEntity(
                    recapId = recap.id!!,
                    startAt = it.startAt,
                    endAt = it.endAt,
                    title = it.title,
                    duration = it.durationMinutes,
                    createdAt = Instant.now()
                )
            }
        timelineRepository.saveAll(timelines)
    }

    private fun saveTopicsInternal(
        recap: RecapEntity,
        name: String,
        activities: List<UserActivityDto>
    ) {
        val topicResponse = generateTopics(name, activities)
        val topics =
            topicResponse.topics.map {
                TopicEntity(
                    recapId = recap.id!!,
                    keyword = it.keyword,
                    title = it.title,
                    content = it.content,
                    createdAt = Instant.now()
                )
            }
        topicRepository.saveAll(topics)
    }

    // AI Generation Methods
    fun generateRecap(
        name: String,
        activities: List<UserActivityDto>
    ) = recapAIClient.generate(RecapType.TODAY_RECAP, name, activities, GeminiRecapResponse::class.java)

    // 이 부분이 UserTimelineDto를 받도록 RecapAIClient 인터페이스와 맞춰져 있어야 합니다.
    fun generateTimeline(
        name: String,
        activities: List<UserTimelineDto>
    ) = recapAIClient.generateTimeline(RecapType.TIMELINE, name, activities, GeminiTimelineResponse::class.java)

    fun generateTopics(
        name: String,
        activities: List<UserActivityDto>
    ) = recapAIClient.generate(RecapType.TOPIC, name, activities, GeminiTopicResponse::class.java)
}
