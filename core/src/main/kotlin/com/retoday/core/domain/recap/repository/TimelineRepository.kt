package com.retoday.core.domain.recap.repository

import com.retoday.core.domain.recap.entity.RecapTimeline
import org.springframework.data.repository.ListCrudRepository

interface TimelineRepository : ListCrudRepository<RecapTimeline, Long> {
    fun findAllByRecapId(recapId: Long): List<RecapTimeline>
}
