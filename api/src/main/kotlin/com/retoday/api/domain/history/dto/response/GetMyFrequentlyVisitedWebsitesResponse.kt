package com.retoday.api.domain.history.dto.response

import com.retoday.core.domain.history.dto.result.GetMyFrequentlyVisitedWebsitesResult
import java.time.LocalDate

data class GetMyFrequentlyVisitedWebsitesResponse(
    val date: LocalDate,
    val websiteAnalyses: List<GetMyFrequentlyVisitedWebsitesResult.WebsiteAnalysis>
) {
    companion object {
        fun from(result: GetMyFrequentlyVisitedWebsitesResult): GetMyFrequentlyVisitedWebsitesResponse =
            with(result) {
                GetMyFrequentlyVisitedWebsitesResponse(
                    date = date,
                    websiteAnalyses = websiteAnalyses
                )
            }
    }
}
