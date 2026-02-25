package com.retoday.core.domain.history.dto.projection

data class WebsiteStatProjection(
    val domain: String,
    val faviconUrl: String?,
    val stayDuration: Long
)
