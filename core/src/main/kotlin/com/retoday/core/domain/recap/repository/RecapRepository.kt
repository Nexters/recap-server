package com.retoday.core.domain.recap.repository

import com.retoday.core.domain.recap.entity.RecapEntity
import com.retoday.core.domain.recap.entity.TopicEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.*

interface RecapRepository : JpaRepository<RecapEntity, Long> {
    // 특정 유저의 특정 날짜 리캡 조회 (하루 단위 조회용)
    fun findByUserIdAndRecapDate(
        userId: Long,
        recapDate: LocalDate
    ): RecapEntity?

    // 특정 유저의 리캡이 해당 날짜에 이미 존재하는지 확인 (생성 전 체크용)
    fun existsByUserIdAndRecapDate(
        userId: Long,
        recapDate: LocalDate
    ): Boolean

    fun findAllByRecapId(recapId: Long): List<TopicEntity>
}
