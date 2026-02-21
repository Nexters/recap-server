package com.retoday.core.domain.history.dto.result

import java.time.LocalDate

data class GetMyCategoryAnalysesResult(
    val date: LocalDate,
    val totalStayDuration: Long,
    val categoryAnalyses: List<CategoryAnalysis>
) {
    data class CategoryAnalysis(
        val categoryName: String,
        val stayDuration: Long,
        val websiteAnalyses: List<WebsiteAnalysis>
    )

    data class WebsiteAnalysis(
        val domain: String,
        val faviconUrl: String?,
        val stayDuration: Long
    )
}
