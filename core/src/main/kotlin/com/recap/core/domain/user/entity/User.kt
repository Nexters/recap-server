package com.recap.core.domain.user.entity

import com.recap.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*

@Entity
data class User(
    @Id
    @Tsid
    val id: Long? = null,
    @Column(unique = true)
    val socialId: String,
    var email: String,
    @Enumerated(EnumType.STRING)
    val provider: Provider,
    @ElementCollection
    @Enumerated(EnumType.STRING)
    val roles: Set<Role> = setOf(Role.MEMBER),
    var isActive: Boolean = true
) : BaseEntity()
