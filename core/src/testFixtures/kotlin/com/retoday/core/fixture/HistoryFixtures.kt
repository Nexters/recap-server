package com.retoday.core.fixture

import com.retoday.core.domain.history.dto.command.HistoryRecordCommand
import com.retoday.core.domain.history.dto.result.HistoryRecordResult
import com.retoday.core.domain.history.entity.History
import com.retoday.core.domain.history.entity.Page
import com.retoday.core.domain.history.entity.Website
import java.time.Instant
import java.time.LocalDate

const val TAB_ID = 1
const val PAGE_ID = 10L
const val WEBSITE_ID = 5L
const val STAY_DURATION = 10
const val USER_EX_DOMAIN = "re-today.com"
const val DOMAIN = "github.com"
const val PAGE_URL = "https://github.com/Nexters/retoday-server"
const val TITLE = "GitHub"
const val DESCRIPTION = "GitHub is where people build software."
const val FAVICON_URL = "https://github.githubassets.com/favicons/favicon.svg"
const val SCROLL_DEPTH = 0

fun createHistoryRecordResult(
    historyId: Long = ID,
    pageId: Long = PAGE_ID,
    websiteId: Long = WEBSITE_ID,
    stayDuration: Int = STAY_DURATION,
    recordedAt: Instant = Instant.now()
): HistoryRecordResult =
    HistoryRecordResult(
        historyId = historyId,
        pageId = pageId,
        websiteId = websiteId,
        stayDuration = stayDuration,
        recordedAt = recordedAt
    )

fun createWebsite(
    id: Long? = WEBSITE_ID,
    domain: String = DOMAIN,
    categoryId: Long? = null,
    faviconUrl: String? = FAVICON_URL
): Website =
    Website(
        id = id,
        domain = domain,
        categoryId = categoryId,
        faviconUrl = faviconUrl
    )

fun createPage(
    id: Long? = PAGE_ID,
    websiteId: Long = WEBSITE_ID,
    url: String = PAGE_URL,
    title: String? = TITLE,
    description: String? = DESCRIPTION
): Page =
    Page(
        id = id,
        websiteId = websiteId,
        url = url,
        title = title,
        description = description
    )

fun createHistory(
    id: Long? = ID,
    userId: Long = ID,
    websiteId: Long = WEBSITE_ID,
    pageId: Long = PAGE_ID,
    visitedAt: Instant = Instant.now().minusSeconds(10),
    closedAt: Instant = Instant.now(),
    stayDuration: Int = STAY_DURATION,
    visitedDate: LocalDate = LocalDate.now(),
    visitedHour: Int = 10,
    isFinal: Boolean = true,
    scrollDepth: Int? = SCROLL_DEPTH
): History =
    History(
        id = id,
        userId = userId,
        websiteId = websiteId,
        pageId = pageId,
        visitedAt = visitedAt,
        closedAt = closedAt,
        stayDuration = stayDuration,
        visitedDate = visitedDate,
        visitedHour = visitedHour,
        isFinal = isFinal,
        scrollDepth = scrollDepth
    )

fun createHistoryRecordCommand(
    tabId: Int = TAB_ID,
    url: String = PAGE_URL,
    visitedAt: Instant = Instant.now().minusSeconds(10),
    closedAt: Instant = Instant.now(),
    title: String? = TITLE,
    description: String? = DESCRIPTION,
    faviconUrl: String? = FAVICON_URL,
    isFinal: Boolean = true,
    scrollDepth: Int? = SCROLL_DEPTH
): HistoryRecordCommand =
    HistoryRecordCommand(
        tabId = tabId,
        url = url,
        visitedAt = visitedAt,
        closedAt = closedAt,
        title = title,
        description = description,
        faviconUrl = faviconUrl,
        isFinal = isFinal,
        scrollDepth = scrollDepth
    )
