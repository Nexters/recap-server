package com.retoday.core.domain.recap.service

import com.retoday.core.domain.recap.dto.response.GeminiRecapResponse
import com.retoday.core.domain.recap.dto.response.GeminiTimelineResponse
import com.retoday.core.domain.recap.dto.response.GeminiTopicResponse
import com.retoday.core.domain.recap.entity.Recap
import com.retoday.core.domain.recap.entity.Section
import com.retoday.core.domain.recap.entity.Timeline
import com.retoday.core.domain.recap.entity.Topic
import com.retoday.core.domain.recap.repository.RecapRepository
import com.retoday.core.domain.recap.repository.SectionRepository
import com.retoday.core.domain.recap.repository.TimelineRepository
import com.retoday.core.domain.recap.repository.TopicRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class PreparedRecapContent(
    val userId: Long,
    val recapDate: LocalDate,
    val title: String,
    val summary: String,
    val startedAt: Instant,
    val closedAt: Instant,
    val model: String,
    val sections: List<GeminiRecapResponse.RecapSection>,
    val topics: List<GeminiTopicResponse.TopicItem>,
    val timelines: List<GeminiTimelineResponse.TimelineItem>
)

@Service
class RecapSaveService(
    private val recapRepository: RecapRepository,
    private val sectionRepository: SectionRepository,
    private val topicRepository: TopicRepository,
    private val timelineRepository: TimelineRepository
) {
    private companion object {
        val TIMELINE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")
    }

    @Transactional
    fun save(prepared: PreparedRecapContent) {
        if (recapRepository.existsByUserIdAndRecapDate(prepared.userId, prepared.recapDate)) return

        val recap =
            Recap(
                userId = prepared.userId,
                recapDate = prepared.recapDate,
                title = prepared.title,
                summary = prepared.summary,
                startedAt = prepared.startedAt,
                closedAt = prepared.closedAt,
                model = prepared.model
            ).let { recapRepository.save(it) }

        val sections =
            prepared.sections.map {
                Section(recapId = recap.id!!, title = it.title, content = it.content)
            }
        sectionRepository.saveAll(sections)

        val topics =
            prepared.topics.map {
                Topic(
                    recapId = recap.id!!,
                    keyword = it.keyword,
                    title = it.title,
                    content = it.content
                )
            }
        topicRepository.saveAll(topics)

        if (prepared.timelines.isEmpty()) return

        val timelines =
            prepared.timelines.map { item ->
                val startedAt = LocalTime.parse(item.startedAt, TIMELINE_TIME_FORMATTER)
                val endedAt = LocalTime.parse(item.endedAt, TIMELINE_TIME_FORMATTER)
                val duration = Duration.between(startedAt, endedAt).toMinutes().toInt()

                Timeline(
                    recapId = recap.id!!,
                    startedAt = startedAt,
                    endedAt = endedAt,
                    title = item.title,
                    durationMinutes = duration
                )
            }
        timelineRepository.saveAll(timelines)
    }
}
