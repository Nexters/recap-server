package com.retoday.core.domain.history.entity

import com.retoday.core.global.entity.BaseEntity
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("history")
data class History(
    val userId: Long,
    val websiteId: Long,
    val pageId: Long,
    val visitedAt: Instant,
    val closedAt: Instant,
    val isClosed: Boolean, // 탭 종료 여부
    val scrollDepth: Int? = null
) : BaseEntity()
