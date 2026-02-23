package com.retoday.core.domain.recap.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import java.time.LocalTime

@Entity
@Table(name = "recap_timeline")
class Timeline(
    @Id
    @Tsid
    val id: Long? = null,
    val recapId: Long,
    val startedAt: LocalTime,
    val endedAt: LocalTime,
    val title: String,
    val durationMinutes: Int
) : BaseEntity()
