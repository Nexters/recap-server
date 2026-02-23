package com.retoday.api.domain.recap.controller

import com.retoday.api.global.annotation.AuthenticationId
import com.retoday.core.domain.recap.dto.response.RecapDetailResponse
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
        @AuthenticationId
        userId: Long,
        @RequestParam date: LocalDate?
    ): ResponseEntity<RecapDetailResponse> {
        val response =
            recapService.generateDailyRecap(userId, date)
                ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(response)
    }
}
