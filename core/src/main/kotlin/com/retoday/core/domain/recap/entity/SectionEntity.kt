package com.retoday.core.domain.recap.entity

import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*

@Entity
@Table(name = "recap_section")
class SectionEntity(
    @Id
    @Tsid
    val id: Long? = null,
    val recapId: Long,
    val title: String,
    val content: String
)
