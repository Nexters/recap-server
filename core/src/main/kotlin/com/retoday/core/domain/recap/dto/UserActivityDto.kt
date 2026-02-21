package com.retoday.core.domain.recap.dto

// AI에 전달할 DTO
data class UserActivityDto(
    val title: String?,
    val description: String?,
    val domain: String,
    val category: String?,
    val duration: Int
)
