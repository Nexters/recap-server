package com.retoday.core.domain.history.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Page(
    @Id
    @Tsid
    val id: Long? = null,
    val websiteId: Long,
    @Column(nullable = false, length = 768, unique = true)
    val url: String,
    @Column(length = 500)
    var title: String? = null,
    @Column(columnDefinition = "TEXT")
    var description: String? = null
) : BaseEntity()
