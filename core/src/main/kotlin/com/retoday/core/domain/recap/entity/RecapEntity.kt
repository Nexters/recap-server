package com.retoday.core.domain.recap.entity

import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(
    name = "recap",
    uniqueConstraints = [
        UniqueConstraint(name = "idx_user_recap_date", columnNames = ["user_id", "recap_date"])
    ]
)
class RecapEntity(
    @Id
    @Tsid
    val id: Long? = null,
    val userId: Long,
    val recapDate: LocalDate,
    var title: String,
    var summary: String,
    var startAt: LocalDateTime,
    var closeAt: LocalDateTime,
    var model: String,
    var status: String = "COMPLETED",
    var createdAt: Instant
)
