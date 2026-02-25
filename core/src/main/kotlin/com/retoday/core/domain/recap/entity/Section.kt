package com.retoday.core.domain.recap.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*

@Entity
@Table(name = "recap_section")
class Section(
    @Id
    @Tsid
    val id: Long? = null,
    val recapId: Long,
    val title: String,
    val content: String
) : BaseEntity()
