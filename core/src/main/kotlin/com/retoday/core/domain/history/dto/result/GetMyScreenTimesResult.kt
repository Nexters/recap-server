package com.retoday.core.domain.history.dto.result

import com.retoday.core.domain.history.dto.query.GetMyScreenTimesQuery
import java.time.LocalDate

data class GetMyScreenTimesResult(
    val period: GetMyScreenTimesQuery.Period,
    val startedAt: LocalDate,
    val endedAt: LocalDate,
    val totalStayDuration: Long,
    val screenTimes: List<ScreenTime>
) {
    data class ScreenTime(
        val startedAt: LocalDate,
        val endedAt: LocalDate,
        val stayDuration: Long
    )
}
