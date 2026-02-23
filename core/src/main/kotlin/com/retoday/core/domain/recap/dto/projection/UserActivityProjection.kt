package com.retoday.core.domain.recap.dto.projection

data class UserActivityProjection(
    val title: String?,
    val description: String?,
    val domain: String,
    val categoryName: String?,
    val stayDuration: Int
)
