package com.retoday.core.domain.recap.repository

import com.retoday.core.domain.recap.entity.Timeline
import org.springframework.data.jpa.repository.JpaRepository

interface TimelineRepository : JpaRepository<Timeline, Long> {
    fun findAllByRecapId(recapId: Long): List<Timeline>
}
