package com.retoday.core.domain.recap.dto.response

// 3번: 많이 둘러본 주제
data class GeminiTopicResponse(
    val topics: List<TopicItem>
) {
    data class TopicItem(
        val keyword: String = "",
        val title: String = "",
        val content: String = ""
    )
}
