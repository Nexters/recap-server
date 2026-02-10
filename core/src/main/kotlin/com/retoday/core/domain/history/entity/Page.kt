package com.retoday.core.domain.history.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant

@Entity
class Page(
    @Id
    @Tsid
    val id: Long? = null,

    @Column(nullable = false)
    val websiteId: Long,

    @Column(nullable = false, length = 2048, unique = true)
    val url: String,

    @Column(length = 500)
    var title: String? = null,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(length = 500)
    var thumbnailUrl: String? = null,

    @Column(nullable = false)
    var totalVisitCount: Long = 0,

    @Column(nullable = false)
    val firstVisitedAt: Instant,

    var lastVisitedAt: Instant? = null
) : BaseEntity() {
    fun incrementVisitCount() {
        totalVisitCount++
    }

    fun updateMetadata(title: String?, description: String?, thumbnailUrl: String?) {
        this.title = title ?: this.title
        this.description = description ?: this.description
        this.thumbnailUrl = thumbnailUrl ?: this.thumbnailUrl
    }

    fun markAsClosed(closedAt: Instant) {
        this.lastVisitedAt = closedAt
    }
}
