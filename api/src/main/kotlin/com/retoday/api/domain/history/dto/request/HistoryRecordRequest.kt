package com.retoday.api.domain.history.dto.request

import com.retoday.core.domain.history.dto.command.HistoryRecordCommand
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.time.Instant

data class HistoryRecordRequest(
    @field:NotNull
    val tabId: Int,
    @field:NotBlank
    @field:Size(max = 2048)
    val url: String,
    @field:NotNull
    val visitedAt: Instant,
    @field:NotNull
    val closedAt: Instant,
    @field:Size(max = 500)
    val title: String?,
    @field:Valid
    val metadata: PageMetadata?,
    @field:NotNull
    val isFinal: Boolean,
    @field:Min(value = 0)
    @field:Max(value = 100)
    val scrollDepth: Int?
) {
    fun toCommand(): HistoryRecordCommand =
        HistoryRecordCommand(
            tabId = tabId,
            url = url,
            visitedAt = visitedAt,
            closedAt = closedAt,
            title = title,
            description = metadata?.description,
            thumbnailUrl = metadata?.thumbnailUrl,
            isFinal = isFinal,
            scrollDepth = scrollDepth
        )
}
