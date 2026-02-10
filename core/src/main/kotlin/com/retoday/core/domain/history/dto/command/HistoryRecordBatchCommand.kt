package com.retoday.core.domain.history.dto.command

data class HistoryRecordBatchCommand(
    val commands: List<HistoryRecordCommand>
)
