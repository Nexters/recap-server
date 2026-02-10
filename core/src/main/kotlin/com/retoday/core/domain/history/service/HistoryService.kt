package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.dto.command.HistoryRecordBatchCommand
import com.retoday.core.domain.history.dto.command.HistoryRecordCommand
import com.retoday.core.domain.history.dto.result.HistoryRecordBatchResult
import com.retoday.core.domain.history.dto.result.HistoryRecordResult
import com.retoday.core.domain.history.exception.InvalidTimeRangeException
import org.springframework.stereotype.Service

@Service
class HistoryService {
    fun recordHistory(
        userId: Long,
        command: HistoryRecordCommand
    ): HistoryRecordResult {
        if (!command.closedAt.isAfter(command.visitedAt)) {
            throw InvalidTimeRangeException("closedAt은 visitedAt보다 이후여야 합니다")
        }

        TODO("미구현")
    }

    fun recordHistoryBatch(
        userId: Long,
        command: HistoryRecordBatchCommand
    ): HistoryRecordBatchResult {
        TODO("미구현")
    }
}
