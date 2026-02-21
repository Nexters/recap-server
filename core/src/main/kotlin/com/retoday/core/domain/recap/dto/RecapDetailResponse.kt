package com.retoday.core.domain.recap.dto

import com.retoday.core.domain.recap.entity.RecapEntity
import com.retoday.core.domain.recap.entity.SectionEntity
import com.retoday.core.domain.recap.entity.TimelineEntity
import com.retoday.core.domain.recap.entity.TopicEntity
import java.time.LocalDate
import java.time.LocalDateTime

data class RecapDetailResponse(
    val id: Long,
    val userId: Long,
    val recapDate: LocalDate,
    val title: String,
    val summary: String,
    val startAt: LocalDateTime,
    val closeAt: LocalDateTime,
    val sections: List<SectionResponse>,
    val timelines: List<TimelineResponse>,
    val topics: List<TopicResponse>
) {
    data class SectionResponse(
        val title: String,
        val content: String
    )

    data class TimelineResponse(
        val startAt: String,
        val endAt: String,
        val title: String,
        val durationMinutes: Int
    )

    data class TopicResponse(
        val keyword: String,
        val title: String,
        val content: String
    )

    companion object {
        fun of(
            recap: RecapEntity,
            sections: List<SectionEntity>,
            timelines: List<TimelineEntity>,
            topics: List<TopicEntity>
        ): RecapDetailResponse =
            RecapDetailResponse(
                id = recap.id!!,
                userId = recap.userId,
                recapDate = recap.recapDate,
                title = recap.title,
                summary = recap.summary,
                startAt = recap.startAt,
                closeAt = recap.closeAt,
                sections = sections.map { SectionResponse(it.title, it.content) },
                timelines = timelines.map { TimelineResponse(it.startAt, it.endAt, it.title, it.durationMinutes) },
                topics = topics.map { TopicResponse(it.keyword, it.title, it.content) }
            )
    }
}
