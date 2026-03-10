package com.retoday.core.global.entity

import com.retoday.core.global.extension.createTsid
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import java.time.Instant

abstract class BaseEntity : Persistable<Long> {
    @Id
    var id: Long = createTsid()
        set

    @CreatedDate
    var createdAt: Instant? = null
        set

    @LastModifiedDate
    var updatedAt: Instant? = null
        set

    var deletedAt: Instant? = null
        set

    override fun getId(): Long = id

    @Transient
    override fun isNew(): Boolean = createdAt == null

    fun softDelete() {
        deletedAt = Instant.now()
    }
}
