package com.retoday.core.domain.recap.dto

// 1번: Today's Recap
data class GeminiRecapResponse(
    val title: String = "",
    val dailySummary: String = "",
    val sections: List<RecapSection> = emptyList()
) {
    data class RecapSection(
        val title: String = "",
        val content: String = ""
    )
}
