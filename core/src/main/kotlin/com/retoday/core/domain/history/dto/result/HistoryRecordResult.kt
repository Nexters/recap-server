package com.retoday.core.domain.history.dto.result

import java.time.Instant

data class HistoryRecordResult(
    val historyId: Long,
    val pageId: Long,
    val websiteId: Long,
    val recordedAt: Instant
)
