package com.retoday.core.domain.history.entity

import com.retoday.core.global.entity.BaseEntity
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("page")
data class Page(
    val websiteId: Long,
    val url: String,
    val title: String? = null,
    val description: String? = null
) : BaseEntity()
