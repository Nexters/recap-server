package com.retoday.api.domain.history.dto.request

import com.retoday.core.domain.history.dto.command.HistoryRecordBatchCommand
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class HistoryRecordBatchRequest(
    @field:NotEmpty
    @field:Size(max = 100, message = "한 번에 최대 100개까지 처리 가능합니다")
    @field:Valid
    val records: List<HistoryRecordRequest>
) {
    fun toCommand(): HistoryRecordBatchCommand =
        HistoryRecordBatchCommand(
            commands = records.map { it.toCommand() }
        )
}
