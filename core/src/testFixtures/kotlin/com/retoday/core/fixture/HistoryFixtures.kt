package com.retoday.core.fixture

import com.retoday.core.domain.history.dto.result.BatchItemResult
import com.retoday.core.domain.history.dto.result.HistoryRecordBatchResult
import com.retoday.core.domain.history.dto.result.HistoryRecordResult
import com.retoday.core.domain.history.entity.Website
import java.time.Instant

const val PAGE_ID = 10L
const val WEBSITE_ID = 5L
const val STAY_DURATION = 100
const val DOMAIN = "re-today.com"

fun createWebsite(
    id: Long? = ID,
    domain: String = DOMAIN
): Website =
    Website(
        id = id,
        domain = domain
    )

fun createHistoryRecordResult(
    historyId: Long = ID,
    pageId: Long = PAGE_ID,
    websiteId: Long = WEBSITE_ID,
    stayDuration: Int = STAY_DURATION,
    recordedAt: Instant = Instant.now()
): HistoryRecordResult =
    HistoryRecordResult(
        historyId = historyId,
        pageId = pageId,
        websiteId = websiteId,
        stayDuration = stayDuration,
        recordedAt = recordedAt
    )

fun createHistoryRecordBatchResult(
    successCount: Int = 3,
    failedCount: Int = 0,
    results: List<BatchItemResult> =
        listOf(
            BatchItemResult(tabId = 1, success = true, historyId = ID),
            BatchItemResult(tabId = 2, success = true, historyId = ID + 1),
            BatchItemResult(tabId = 3, success = true, historyId = ID + 2)
        )
): HistoryRecordBatchResult =
    HistoryRecordBatchResult(
        successCount = successCount,
        failedCount = failedCount,
        results = results
    )

fun createHistoryRecordBatchResultWithFailures(
    successCount: Int = 2,
    failedCount: Int = 1,
    results: List<BatchItemResult> =
        listOf(
            BatchItemResult(
                tabId = 1,
                success = true,
                historyId = ID,
                errorCode = null,
                errorMessage = null
            ),
            BatchItemResult(
                tabId = 2,
                success = false,
                historyId = null,
                errorCode = "DUPLICATE_HISTORY",
                errorMessage = "이미 저장된 히스토리입니다. tabId: 2, url: https://github.com/Nexters/retoday-server"
            ),
            BatchItemResult(
                tabId = 3,
                success = true,
                historyId = ID + 2,
                errorCode = null,
                errorMessage = null
            )
        )
): HistoryRecordBatchResult =
    HistoryRecordBatchResult(
        successCount = successCount,
        failedCount = failedCount,
        results = results
    )
