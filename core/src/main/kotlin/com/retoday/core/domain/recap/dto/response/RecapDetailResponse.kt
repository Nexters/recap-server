package com.retoday.core.domain.recap.dto.response

import com.retoday.core.domain.recap.entity.Recap
import com.retoday.core.domain.recap.entity.RecapStatus
import com.retoday.core.domain.recap.entity.Section
import com.retoday.core.domain.recap.entity.Timeline
import com.retoday.core.domain.recap.entity.Topic
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class RecapDetailResponse(
    val id: Long,
    val userId: Long,
    val recapDate: LocalDate,
    val status: RecapStatus,
    val title: String,
    val summary: String,
    val imageUrl: String? = null,
    val startedAt: LocalDateTime,
    val closedAt: LocalDateTime,
    val sections: List<SectionResponse>,
    val timelines: List<TimelineResponse>,
    val topics: List<TopicResponse>
) {
    data class SectionResponse(
        val title: String,
        val content: String
    )

    data class TimelineResponse(
        val startedAt: String,
        val endedAt: String,
        val title: String,
        val durationMinutes: Int
    )

    data class TopicResponse(
        val keyword: String,
        val title: String,
        val content: String
    )

    companion object {
        private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        fun of(
            recap: Recap,
            sections: List<Section>,
            timelines: List<Timeline>,
            topics: List<Topic>,
            zoneId: ZoneId
        ): RecapDetailResponse =
            RecapDetailResponse(
                id = recap.id!!,
                userId = recap.userId,
                recapDate = recap.recapDate,
                status = recap.status,
                title = recap.title,
                summary = recap.summary,
                imageUrl = recap.imageUrl,
                startedAt = LocalDateTime.ofInstant(recap.startedAt, zoneId),
                closedAt = LocalDateTime.ofInstant(recap.closedAt, zoneId),
                sections = sections.map { SectionResponse(it.title, it.content) },
                timelines =
                    timelines.map {
                        TimelineResponse(
                            startedAt = it.startedAt.format(TIME_FORMATTER),
                            endedAt = it.endedAt.format(TIME_FORMATTER),
                            title = it.title,
                            durationMinutes = it.durationMinutes
                        )
                    },
                topics = topics.map { TopicResponse(it.keyword, it.title, it.content) }
            )
    }
}
