package com.retoday.api.domain.history.dto.response

import com.retoday.core.domain.history.dto.result.BatchItemResult

data class BatchResult(
    val tabId: Int,
    val success: Boolean,
    val historyId: Long? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
) {
    companion object {
        fun from(result: BatchItemResult): BatchResult =
            with(result) {
                BatchResult(
                    tabId = tabId,
                    success = success,
                    historyId = historyId,
                    errorCode = errorCode,
                    errorMessage = errorMessage
                )
            }
    }
}
