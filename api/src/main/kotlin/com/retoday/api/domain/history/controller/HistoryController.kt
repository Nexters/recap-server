package com.retoday.api.domain.history.controller

import com.retoday.api.domain.history.dto.request.HistoryRecordRequest
import com.retoday.api.domain.history.dto.response.GetMyCategoryAnalysesResponse
import com.retoday.api.domain.history.dto.response.GetMyScreenTimesResponse
import com.retoday.api.domain.history.dto.response.HistoryRecordResponse
import com.retoday.api.global.annotation.AuthenticationId
import com.retoday.core.domain.history.dto.query.GetMyCategoryAnalysisQuery
import com.retoday.core.domain.history.dto.query.GetMyScreenTimesQuery
import com.retoday.core.domain.history.service.HistoryService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1")
class HistoryController(
    private val historyService: HistoryService
) {
    @PostMapping("/histories")
    fun recordHistory(
        @AuthenticationId
        userId: Long,
        @Valid
        @RequestBody
        request: HistoryRecordRequest
    ): HistoryRecordResponse =
        historyService
            .recordHistory(userId, request.toCommand())
            .let { HistoryRecordResponse.from(it) }

    @GetMapping("/users/me/screen-times")
    fun getMyScreenTimes(
        @AuthenticationId
        userId: Long,
        @RequestParam
        date: LocalDate,
        @RequestParam
        period: GetMyScreenTimesQuery.Period
    ): GetMyScreenTimesResponse =
        historyService
            .getMyScreenTimes(
                userId,
                GetMyScreenTimesQuery(
                    date = date,
                    period = period
                )
            ).let { GetMyScreenTimesResponse.from(it) }

    @GetMapping("/users/me/category-analyses")
    fun getMyCategoryAnalyses(
        @AuthenticationId
        userId: Long,
        @RequestParam
        date: LocalDate
    ): GetMyCategoryAnalysesResponse =
        historyService
            .getMyCategoryAnalyses(userId, GetMyCategoryAnalysisQuery(date = date))
            .let { GetMyCategoryAnalysesResponse.from(it) }
}
