package com.retoday.api.domain.history.dto.response

import com.retoday.core.domain.history.dto.query.GetMyScreenTimesQuery
import com.retoday.core.domain.history.dto.result.GetMyScreenTimesResult
import java.time.LocalDate

data class GetMyScreenTimesResponse(
    val period: GetMyScreenTimesQuery.Period,
    val startedAt: LocalDate,
    val endedAt: LocalDate,
    val totalStayDuration: Long,
    val screenTimes: List<ScreenTimeResponse>
) {
    data class ScreenTimeResponse(
        val startedAt: LocalDate,
        val endedAt: LocalDate,
        val stayDuration: Long
    )

    companion object {
        fun from(result: GetMyScreenTimesResult): GetMyScreenTimesResponse =
            with(result) {
                GetMyScreenTimesResponse(
                    period = period,
                    startedAt = startedAt,
                    endedAt = endedAt,
                    totalStayDuration = totalStayDuration,
                    screenTimes =
                        screenTimes.map {
                            ScreenTimeResponse(
                                startedAt = it.startedAt,
                                endedAt = it.endedAt,
                                stayDuration = it.stayDuration
                            )
                        }
                )
            }
    }
}
