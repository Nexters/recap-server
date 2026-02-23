package com.retoday.core.domain.recap.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(
    name = "recap",
    uniqueConstraints = [
        UniqueConstraint(name = "idx_user_recap_date", columnNames = ["user_id", "recap_date"])
    ]
)
class Recap(
    @Id
    @Tsid
    val id: Long? = null,
    val userId: Long,
    val recapDate: LocalDate,
    var title: String,
    var summary: String,
    var startedAt: Instant,
    var closedAt: Instant,
    var model: String,
    @Enumerated(EnumType.STRING)
    var status: RecapStatus = RecapStatus.COMPLETED
) : BaseEntity()
