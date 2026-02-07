package com.retoday.core.domain.website.entity

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
    @Column(unique = true)
    val domain: String
) : BaseEntity()
