package com.retoday.core.domain.history.entity

import com.retoday.core.global.entity.BaseEntity
import org.springframework.data.relational.core.mapping.Table

@Table("website_category")
data class WebsiteCategory(
    val code: WebsiteCategoryCode,
    val name: String
) : BaseEntity()
