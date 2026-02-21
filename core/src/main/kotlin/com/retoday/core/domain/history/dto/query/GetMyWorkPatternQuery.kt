package com.retoday.core.domain.history.dto.query

import java.time.LocalDate

data class GetMyWorkPatternQuery(
    val date: LocalDate
) {
    enum class TimeSlot(
        val startedAt: Long,
        val endedAt: Long
    ) {
        DAWN(0, 6),
        MORNING(6, 12),
        DAYTIME(12, 18),
        EVENING(18, 24)
    }
}
