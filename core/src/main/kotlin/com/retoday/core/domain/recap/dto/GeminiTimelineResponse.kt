package com.retoday.core.domain.recap.dto

// 2번: AI 타임라인
data class GeminiTimelineResponse(
    val timelines: List<TimelineItem> = emptyList()
)

data class TimelineItem(
    val startAt: String = "", // "HH:mm"
    val endAt: String = "",
    val title: String = "",
    val durationMinutes: Int = 0
)
