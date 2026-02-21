package com.retoday.api.domain.history.dto.response

import com.retoday.core.domain.history.dto.result.HistoryRecordResult
import java.time.Instant

data class HistoryRecordResponse(
    val historyId: Long,
    val pageId: Long,
    val websiteId: Long,
    val recordedAt: Instant
) {
    companion object {
        fun from(result: HistoryRecordResult): HistoryRecordResponse =
            with(result) {
                HistoryRecordResponse(
                    historyId = historyId,
                    pageId = pageId,
                    websiteId = websiteId,
                    recordedAt = recordedAt
                )
            }
    }
}
