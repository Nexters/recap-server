package com.retoday.core.domain.history.dto.query

import java.time.LocalDate

data class GetMyFrequentlyVisitedWebsitesQuery(
    val date: LocalDate,
    val limit: Int
)
