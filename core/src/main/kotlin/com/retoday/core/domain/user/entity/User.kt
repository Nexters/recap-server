package com.retoday.core.domain.user.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["provider", "social_id"])])
class User(
    @Id
    @Tsid
    val id: Long? = null,
    val socialId: String,
    var email: String,
    @Enumerated(EnumType.STRING)
    val provider: Provider,
    @ElementCollection
    @Enumerated(EnumType.STRING)
    val roles: Set<Role> = setOf(Role.MEMBER),
    var isActive: Boolean = true
) : BaseEntity()
