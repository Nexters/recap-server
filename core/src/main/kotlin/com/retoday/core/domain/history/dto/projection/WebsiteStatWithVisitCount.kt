package com.retoday.core.domain.history.dto.projection

data class WebsiteStatWithVisitCount(
    val domain: String,
    val faviconUrl: String?,
    val visitCount: Long,
    val stayDuration: Long
)
