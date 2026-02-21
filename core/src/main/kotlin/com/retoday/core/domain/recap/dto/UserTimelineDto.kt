package com.retoday.core.domain.recap.dto

import java.time.Instant

class UserTimelineDto(
    val title: String?,
    val description: String?,
    val categoryName: String?,
    val visitedAt: Instant,
    val closedAt: Instant
)
