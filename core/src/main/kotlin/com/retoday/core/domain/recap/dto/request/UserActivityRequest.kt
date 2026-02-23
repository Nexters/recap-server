package com.retoday.core.domain.recap.dto.request

data class UserActivityRequest(
    val title: String?,
    val description: String?,
    val domain: String,
    val categoryName: String?,
    val stayDuration: Int
)
