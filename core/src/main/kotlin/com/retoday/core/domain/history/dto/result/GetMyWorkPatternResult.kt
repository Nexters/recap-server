package com.retoday.core.domain.history.dto.result

import com.retoday.core.domain.history.dto.query.GetMyWorkPatternQuery
import java.time.LocalDate

data class GetMyWorkPatternResult(
    val date: LocalDate,
    val counts: Map<GetMyWorkPatternQuery.TimeSlot, Long>
)
