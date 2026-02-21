package com.retoday.core.global.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: Instant = Instant.EPOCH
        protected set

    @LastModifiedDate
    var updatedAt: Instant? = null
        protected set

    var deletedAt: Instant? = null
        protected set

    fun softDelete() {
        this.deletedAt = Instant.now()
    }

    fun isDeleted(): Boolean = deletedAt != null
}
