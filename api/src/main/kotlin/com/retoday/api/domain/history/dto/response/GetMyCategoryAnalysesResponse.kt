package com.retoday.api.domain.history.dto.response

import com.retoday.core.domain.history.dto.result.GetMyCategoryAnalysesResult
import java.time.LocalDate

data class GetMyCategoryAnalysesResponse(
    val date: LocalDate,
    val categoryAnalyses: List<GetMyCategoryAnalysesResult.CategoryAnalysis>
) {
    companion object {
        fun from(result: GetMyCategoryAnalysesResult): GetMyCategoryAnalysesResponse =
            with(result) {
                GetMyCategoryAnalysesResponse(
                    date = date,
                    categoryAnalyses = categoryAnalyses
                )
            }
    }
}
