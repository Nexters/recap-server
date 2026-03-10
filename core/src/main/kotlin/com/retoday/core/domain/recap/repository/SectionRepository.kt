package com.retoday.core.domain.recap.repository

import com.retoday.core.domain.recap.entity.Section
import org.springframework.data.repository.ListCrudRepository

interface SectionRepository : ListCrudRepository<Section, Long> {
    // 특정 리캡에 속한 모든 섹션 조회
    fun findAllByRecapId(recapId: Long): List<Section>
}
