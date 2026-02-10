package com.retoday.core.domain.history.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant

@Entity
class WebsiteCategory(
    @Id
    @Tsid
    val id: Long? = null,
    @Column(nullable = false, length = 50, unique = true)
    val name: String,
    var deletedAt: Instant? = null
) : BaseEntity()
