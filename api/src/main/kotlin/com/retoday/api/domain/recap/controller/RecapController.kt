package com.retoday.api.domain.recap.controller

import com.retoday.core.domain.recap.service.RecapService
import org.springframework.format.annotation.DateTimeFormat
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
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate?
    ): ResponseEntity<Unit> {
        val targetDate = date ?: LocalDate.now().minusDays(1)

        recapService.createDailyRecap(userId, nickname, targetDate)

        return ResponseEntity.ok().build()
    }
}
