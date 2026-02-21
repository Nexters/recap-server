package com.retoday.core.domain.history.dto.result

import java.time.LocalDate

data class GetMyFrequentlyVisitedWebsitesResult(
    val date: LocalDate,
    val websiteAnalyses: List<WebsiteAnalysis>
) {
    data class WebsiteAnalysis(
        val domain: String,
        val faviconUrl: String?,
        val visitCount: Long,
        val stayDuration: Long
    )
}
