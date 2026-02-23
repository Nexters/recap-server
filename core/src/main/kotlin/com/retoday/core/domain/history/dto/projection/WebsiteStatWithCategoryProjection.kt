package com.retoday.core.domain.history.dto.projection

data class WebsiteStatWithCategoryProjection(
    val domain: String,
    val faviconUrl: String?,
    val categoryName: String?,
    val stayDuration: Long
)
