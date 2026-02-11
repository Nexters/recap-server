package com.retoday.core.domain.history.dto.command

import com.retoday.core.global.util.UrlUtils
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class HistoryRecordCommand(
    val tabId: Int,
    val url: String,
    val visitedAt: Instant,
    val closedAt: Instant,
    val title: String?,
    val description: String?,
    val faviconUrl: String?,
    val isFinal: Boolean,
    val scrollDepth: Int?
) {
    fun getStayDuration(): Int =
        java.time.Duration
            .between(visitedAt, closedAt)
            .seconds
            .toInt()

    fun getDomain(): String = UrlUtils.extractDomain(url)

    fun getNormalizedUrl(): String = UrlUtils.normalizeUrl(url)

    fun getVisitedDate(userTimeZone: ZoneId): LocalDate = visitedAt.atZone(userTimeZone).toLocalDate()

    fun getVisitedHour(userTimeZone: ZoneId): Int = visitedAt.atZone(userTimeZone).hour
}
