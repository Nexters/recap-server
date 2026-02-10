package com.retoday.api.domain.history.controller

import com.retoday.api.domain.history.dto.request.HistoryRecordBatchRequest
import com.retoday.api.domain.history.dto.request.HistoryRecordRequest
import com.retoday.api.domain.history.dto.response.HistoryRecordBatchResponse
import com.retoday.api.domain.history.dto.response.HistoryRecordResponse
import com.retoday.api.global.annotation.AuthenticationId
import com.retoday.core.domain.history.service.HistoryService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/histories")
class HistoryController(
    private val historyService: HistoryService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun recordHistory(
        @AuthenticationId userId: Long,
        @Valid @RequestBody request: HistoryRecordRequest
    ): HistoryRecordResponse =
        historyService
            .recordHistory(userId, request.toCommand())
            .let { HistoryRecordResponse.from(it) }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    fun recordHistoryBatch(
        @AuthenticationId userId: Long,
        @Valid @RequestBody request: HistoryRecordBatchRequest
    ): HistoryRecordBatchResponse =
        historyService
            .recordHistoryBatch(userId, request.toCommand())
            .let { HistoryRecordBatchResponse.from(it) }
}
