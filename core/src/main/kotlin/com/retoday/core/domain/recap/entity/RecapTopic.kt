package com.retoday.core.domain.recap.entity

import com.retoday.core.global.entity.BaseEntity
import org.springframework.data.relational.core.mapping.Table

@Table("recap_topic")
data class RecapTopic(
    val recapId: Long,
    val keyword: String,
    val title: String,
    val content: String
) : BaseEntity()
