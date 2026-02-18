package com.retoday.core.domain.history.dto.projection

data class WebsiteStatWithCategory(
    val domain: String,
    val faviconUrl: String?,
    val categoryName: String?,
    val stayDuration: Long
)
