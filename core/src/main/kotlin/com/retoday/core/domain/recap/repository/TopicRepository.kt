package com.retoday.core.domain.recap.repository

import com.retoday.core.domain.recap.entity.Topic
import org.springframework.data.jpa.repository.JpaRepository

interface TopicRepository : JpaRepository<Topic, Long> {
    // 특정 리캡에 생성된 모든 주제(Topic) 조회
    fun findAllByRecapId(recapId: Long): List<Topic>
}
