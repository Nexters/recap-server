package com.retoday.core.domain.recap.repository

import com.retoday.core.domain.recap.entity.Timeline
import org.springframework.data.repository.ListCrudRepository

interface TimelineRepository : ListCrudRepository<Timeline, Long> {
    fun findAllByRecapId(recapId: Long): List<Timeline>
}
