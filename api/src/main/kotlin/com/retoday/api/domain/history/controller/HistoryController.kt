package com.retoday.api.domain.history.controller

import com.retoday.api.domain.history.dto.request.HistoryRecordRequest
import com.retoday.api.domain.history.dto.response.HistoryRecordResponse
import com.retoday.api.global.annotation.AuthenticationId
import com.retoday.core.domain.history.service.HistoryService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/histories")
class HistoryController(
    private val historyService: HistoryService
) {
    @PostMapping
    fun recordHistory(
        @AuthenticationId userId: Long,
        @Valid @RequestBody request: HistoryRecordRequest
    ): HistoryRecordResponse =
        historyService
            .recordHistory(userId, request.toCommand())
            .let { HistoryRecordResponse.from(it) }
}
