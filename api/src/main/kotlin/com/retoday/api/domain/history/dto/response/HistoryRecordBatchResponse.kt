package com.retoday.api.domain.history.dto.response

import com.retoday.core.domain.history.dto.result.BatchItemResult
import com.retoday.core.domain.history.dto.result.HistoryRecordBatchResult

data class HistoryRecordBatchResponse(
    val successCount: Int,
    val failedCount: Int,
    val results: List<BatchItemResult>
) {
    companion object {
        fun from(result: HistoryRecordBatchResult): HistoryRecordBatchResponse =
            HistoryRecordBatchResponse(
                successCount = result.successCount,
                failedCount = result.failedCount,
                results = result.results
            )
    }
}
