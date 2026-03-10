package com.retoday.core.domain.recap.entity

import com.retoday.core.global.entity.BaseEntity
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalTime

@Table("timeline")
data class Timeline(
    val recapId: Long,
    val startedAt: LocalTime,
    val endedAt: LocalTime,
    val title: String,
    val durationMinutes: Int
) : BaseEntity()
