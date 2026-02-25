package com.retoday.core.domain.history.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id

@Entity
class WebsiteCategory(
    @Id
    @Tsid
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30, unique = true)
    val code: WebsiteCategoryCode,
    @Column(nullable = false, length = 50, unique = true)
    val name: String
) : BaseEntity()
