package com.retoday.core.domain.history.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(
    indexes = [
        Index(name = "idx_user_id_visited_at", columnList = "user_id, visited_at")
    ]
)
class History(
    @Id
    @Tsid
    val id: Long? = null,
    val userId: Long,
    val websiteId: Long,
    val pageId: Long,
    val visitedAt: Instant,
    val closedAt: Instant,
    val stayDuration: Int, // closedAt - visitedAt (초)
    val visitedDate: LocalDate,
    val visitedHour: Int, // 0-23
    val isClosed: Boolean, // 탭 종료 여부
    val scrollDepth: Int? = null
) : BaseEntity()
