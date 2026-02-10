package com.retoday.core.domain.history.dto.result

data class HistoryRecordBatchResult(
    val successCount: Int,
    val failedCount: Int,
    val results: List<BatchItemResult>
)
