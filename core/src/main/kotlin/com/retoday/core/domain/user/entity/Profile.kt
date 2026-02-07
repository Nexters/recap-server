package com.retoday.core.domain.user.entity

import com.retoday.core.global.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.time.LocalTime

@Entity
class Profile(
    @Id
    @Tsid
    val id: Long? = null,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    @Enumerated(EnumType.STRING)
    val timeZone: TimeZone = TimeZone.SEOUL,
    val recapPeriod: LocalTime? = null
) : BaseEntity()
