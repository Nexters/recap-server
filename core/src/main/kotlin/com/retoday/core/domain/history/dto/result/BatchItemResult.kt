package com.retoday.core.domain.history.dto.result

data class BatchItemResult(
    val tabId: Int,
    val success: Boolean,
    val historyId: Long? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
)
