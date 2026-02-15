package com.retoday.api.fixture

import com.retoday.api.domain.history.dto.request.HistoryRecordRequest
import com.retoday.api.domain.history.dto.request.PageMetadata
import java.time.Instant

const val TAB_ID = 1
const val HISTORY_URL = "https://github.com/Nexters/retoday-server"
const val HISTORY_DOMAIN = "github.com"
val VISITED_AT: Instant = Instant.parse("2026-02-07T07:11:47.403Z")
val CLOSED_AT: Instant = Instant.parse("2026-02-07T07:11:50.887Z")
const val TITLE = "GitHub"
const val DESCRIPTION = "GitHub is where people build software."
const val FAVICON_URL = "https://github.githubassets.com/favicons/favicon.svg"
const val IS_FINAL = true
const val SCROLL_DEPTH = 75

fun createHistoryRecordRequest(
    tabId: Int = TAB_ID,
    url: String = HISTORY_URL,
    visitedAt: Instant = VISITED_AT,
    closedAt: Instant = CLOSED_AT,
    title: String? = TITLE,
    description: String? = DESCRIPTION,
    faviconUrl: String? = FAVICON_URL,
    isClosed: Boolean = IS_FINAL,
    scrollDepth: Int? = SCROLL_DEPTH
): HistoryRecordRequest =
    HistoryRecordRequest(
        tabId = tabId,
        url = url,
        visitedAt = visitedAt,
        closedAt = closedAt,
        title = title,
        metadata =
            PageMetadata(
                description = description,
                faviconUrl = faviconUrl
            ),
        isClosed = isClosed,
        scrollDepth = scrollDepth
    )
