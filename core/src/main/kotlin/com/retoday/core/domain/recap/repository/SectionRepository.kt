package com.retoday.core.domain.recap.repository

import com.retoday.core.domain.recap.entity.SectionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SectionRepository : JpaRepository<SectionEntity, Long> {
    // 특정 리캡에 속한 모든 섹션 조회
    fun findAllByRecapId(recapId: Long): List<SectionEntity>
}
