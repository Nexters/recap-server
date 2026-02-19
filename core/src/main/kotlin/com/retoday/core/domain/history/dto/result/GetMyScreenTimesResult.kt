package com.retoday.core.domain.history.dto.result

import com.retoday.core.domain.history.dto.query.GetMyScreenTimesQuery
import java.time.LocalDate
import java.time.LocalDateTime

data class GetMyScreenTimesResult(
    val period: GetMyScreenTimesQuery.Period,
    val startedAt: LocalDate,
    val endedAt: LocalDate,
    val totalStayDuration: Long,
    val screenTimes: List<ScreenTime>
) {
    data class ScreenTime(
        val startedAt: LocalDateTime,
        val endedAt: LocalDateTime,
        val stayDuration: Long
    )
}
