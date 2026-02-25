package com.retoday.core.domain.recap.dto.response

import com.fasterxml.jackson.annotation.JsonAlias

data class GeminiTimelineResponse(
    val timelines: List<TimelineItem> = emptyList()
) {
    data class TimelineItem(
        @JsonAlias("startAt")
        val startedAt: String,
        @JsonAlias("endAt")
        val endedAt: String,
        val title: String,
        val durationMinutes: Int
    )
}
