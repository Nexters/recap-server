package com.retoday.core.domain.user.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "domain"])])
class UserExcludedWebsiteDomain(
    @Id
    @Tsid
    val id: Long? = null,
    val userId: Long,
    val domain: String
) : BaseEntity()
