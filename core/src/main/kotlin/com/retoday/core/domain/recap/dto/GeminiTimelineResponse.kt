package com.retoday.core.domain.recap.dto

data class GeminiTimelineResponse(
    val timelines: List<TimelineItem> = emptyList()
) {
    data class TimelineItem(
        val startAt: String,
        val endAt: String,
        val title: String,
        val durationMinutes: Int
    )
}
