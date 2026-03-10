package com.retoday.core.domain.history.entity

import com.retoday.core.global.entity.BaseEntity
import org.springframework.data.relational.core.mapping.Table

@Table("website")
data class Website(
    val domain: String,
    var categoryId: Long? = null,
    var faviconUrl: String? = null
) : BaseEntity() {
    fun updateCategory(categoryId: Long?) {
        this.categoryId = categoryId
    }

    fun updateFaviconUrl(faviconUrl: String) {
        this.faviconUrl = faviconUrl
    }
}
