package com.retoday.core.domain.user.dto.projection

import com.retoday.core.domain.user.entity.Language
import com.retoday.core.domain.user.entity.TimeZone
import java.time.Instant
import java.time.LocalTime

data class ProfileWithEmailProjection(
    val id: Long,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val timeZone: TimeZone,
    val recapPeriod: LocalTime?,
    val language: Language,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val deletedAt: Instant?,
    val email: String
)
