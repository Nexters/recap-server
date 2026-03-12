package com.retoday.core.domain.recap.entity

import com.retoday.core.global.entity.BaseEntity
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDate

@Table("recap")
data class Recap(
    val userId: Long,
    val recapDate: LocalDate,
    val title: String,
    val summary: String,
    val imageUrl: String? = null,
    val startedAt: Instant,
    val closedAt: Instant,
    val model: String,
    val status: RecapStatus = RecapStatus.COMPLETED
) : BaseEntity()
