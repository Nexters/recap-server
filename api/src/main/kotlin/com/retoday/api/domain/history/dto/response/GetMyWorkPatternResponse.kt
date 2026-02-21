package com.retoday.api.domain.history.dto.response

import com.retoday.core.domain.history.dto.query.GetMyWorkPatternQuery
import com.retoday.core.domain.history.dto.result.GetMyWorkPatternResult
import java.time.LocalDate

data class GetMyWorkPatternResponse(
    val date: LocalDate,
    val counts: Map<GetMyWorkPatternQuery.TimeSlot, Long>
) {
    companion object {
        fun from(result: GetMyWorkPatternResult): GetMyWorkPatternResponse =
            with(result) {
                GetMyWorkPatternResponse(
                    date = date,
                    counts = counts
                )
            }
    }
}
