package com.retoday.core.domain.recap.dto

data class GeminiRecapResponse(
    val title: String = "",
    val subtitle: String = "",
    val daily_summary: String = "",
    val sections: List<RecapSection> = emptyList()
)

data class RecapSection(
    val title: String = "",
    val content: String = ""
)
