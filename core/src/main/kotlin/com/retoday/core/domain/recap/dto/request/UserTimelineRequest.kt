package com.retoday.core.domain.recap.dto.request

import java.time.Instant

data class UserTimelineRequest(
    val title: String?,
    val description: String?,
    val categoryName: String?,
    val visitedAt: Instant?,
    val closedAt: Instant?
)
