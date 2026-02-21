package com.retoday.core.domain.history.event

data class WebsiteCategoryClassificationEvent(
    val websiteId: Long,
    val domain: String
)
