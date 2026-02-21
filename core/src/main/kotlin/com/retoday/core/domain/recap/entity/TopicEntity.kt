package com.retoday.core.domain.recap.entity

import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "recap_topic")
class TopicEntity(
    @Id
    @Tsid
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    val recapId: Long,
    val keyword: String,
    val title: String,
    val content: String,
    val createdAt: Instant
)
