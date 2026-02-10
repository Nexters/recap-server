package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.dto.command.HistoryRecordBatchCommand
import com.retoday.core.domain.history.dto.command.HistoryRecordCommand
import com.retoday.core.domain.history.dto.result.HistoryRecordBatchResult
import com.retoday.core.domain.history.dto.result.HistoryRecordResult
import org.springframework.stereotype.Service

@Service
class HistoryService {
    fun recordHistory(
        userId: Long,
        command: HistoryRecordCommand
    ): HistoryRecordResult {
        TODO("미구현")
    }

    fun recordHistoryBatch(
        userId: Long,
        command: HistoryRecordBatchCommand
    ): HistoryRecordBatchResult {
        TODO("미구현")
    }
}
