package com.retoday.core.domain.recap.dto

import java.time.LocalDateTime

class UserTimelineDto(
    val title: String?,
    val description: String?,
    val categoryName: String?,
    val visitedAt: LocalDateTime,
    val closedAt: LocalDateTime
)
