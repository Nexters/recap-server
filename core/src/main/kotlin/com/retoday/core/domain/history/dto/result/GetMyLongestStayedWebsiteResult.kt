package com.retoday.core.domain.history.dto.result

import java.time.LocalDate

data class GetMyLongestStayedWebsiteResult(
    val date: LocalDate,
    val domain: String?,
    val faviconUrl: String?,
    val stayDuration: Long
)
