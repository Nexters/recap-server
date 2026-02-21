package com.retoday.core.domain.history.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Website(
    @Id
    @Tsid
    val id: Long? = null,
    @Column(nullable = false, unique = true)
    val domain: String,
    var categoryId: Long? = null,
    @Column(length = 500)
    var faviconUrl: String? = null
) : BaseEntity() {
    fun updateCategory(newCategoryId: Long?) {
        this.categoryId = newCategoryId
    }

    fun updateFaviconUrl(newFaviconUrl: String) {
        this.faviconUrl = newFaviconUrl
    }
}
