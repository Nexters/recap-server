package com.retoday.api.domain.recap.controller

import com.retoday.core.domain.recap.dto.RecapDetailResponse
import com.retoday.core.domain.recap.service.RecapService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/recaps")
class RecapController(
    private val recapService: RecapService
) {
    @PostMapping("/generate")
    fun generateDailyRecap(
        @RequestParam userId: Long,
        @RequestParam nickname: String,
        @RequestParam date: LocalDate?
    ): ResponseEntity<RecapDetailResponse> {
        val targetDate = date ?: LocalDate.now().minusDays(1)

        // 1. 생성 (Write)
        recapService.createDailyRecap(userId, nickname, targetDate)

        // 2. 조회 (Read)
        val response =
            recapService.getRecapDetail(userId, targetDate)
                ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(response)
    }
}
