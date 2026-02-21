package com.retoday.core.domain.recap.entity

import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import java.time.Duration
import java.time.Instant

@Entity
@Table(name = "recap_timeline")
class TimelineEntity(
    @Id
    @Tsid
    val id: Long? = null,
    val recapId: Long,
    val startAt: String,
    val endAt: String,
    val title: String,
    val durationMinutes: Int,
    val createdAt: Instant = Instant.now()
)
