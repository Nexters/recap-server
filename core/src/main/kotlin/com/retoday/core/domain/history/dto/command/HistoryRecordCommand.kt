package com.retoday.core.domain.history.dto.command

import com.retoday.core.domain.history.exception.InvalidUrlException
import java.time.Instant

data class HistoryRecordCommand(
    val tabId: Int,
    val url: String,
    val visitedAt: Instant,
    val closedAt: Instant,
    val title: String?,
    val description: String?,
    val thumbnailUrl: String?,
    val isFinal: Boolean,
    val scrollDepth: Int?
) {
    fun getStayDuration(): Int =
        java.time.Duration
            .between(visitedAt, closedAt)
            .seconds
            .toInt()

    fun getDomain(): String =
        try {
            java.net
                .URI(url)
                .host
                .removePrefix("www.")
        } catch (e: Exception) {
            throw InvalidUrlException(url)
        }
}
