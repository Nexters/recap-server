package com.retoday.core.domain.recap.repository

import com.retoday.core.domain.recap.entity.TimelineEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TimelineRepository : JpaRepository<TimelineEntity, Long> {
    fun findAllByRecapId(recapId: Long): List<TimelineEntity>
}
