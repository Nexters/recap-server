package com.retoday.core.domain.recap.dto.projection

import java.time.Instant

data class UserTimelineProjection(
    val title: String?,
    val description: String?,
    val categoryName: String?,
    val visitedAt: Instant?,
    val closedAt: Instant?
)
