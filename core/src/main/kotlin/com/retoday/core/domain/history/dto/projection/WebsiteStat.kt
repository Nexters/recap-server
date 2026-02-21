package com.retoday.core.domain.history.dto.projection

data class WebsiteStat(
    val domain: String,
    val faviconUrl: String?,
    val stayDuration: Long
)
