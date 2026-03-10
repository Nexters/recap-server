package com.retoday.core.domain.recap.entity

import com.retoday.core.global.entity.BaseEntity
import org.springframework.data.relational.core.mapping.Table

@Table("section")
data class Section(
    val recapId: Long,
    val title: String,
    val content: String
) : BaseEntity()
