package com.retoday.core.domain.history.dto.command

import com.retoday.core.global.util.UrlUtils
import java.time.Instant

data class HistoryRecordCommand(
    val tabId: Int,
    val url: String,
    val visitedAt: Instant,
    val closedAt: Instant,
    val title: String?,
    val description: String?,
    val faviconUrl: String?,
    val isClosed: Boolean,
    val scrollDepth: Int?
) {
    val domain: String
        get() = UrlUtils.extractDomain(url)

    val normalizedUrl: String
        get() = UrlUtils.normalizeUrl(url)
}
