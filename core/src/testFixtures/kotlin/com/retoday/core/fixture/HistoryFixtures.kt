package com.retoday.core.fixture

import com.retoday.core.domain.history.dto.command.HistoryRecordCommand
import com.retoday.core.domain.history.dto.query.GetMyScreenTimesQuery
import com.retoday.core.domain.history.dto.result.GetMyScreenTimesResult
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

fun createGetMyScreenTimesResult(date: LocalDate = LocalDate.parse("2026-02-13")): GetMyScreenTimesResult =
    GetMyScreenTimesResult(
        period = GetMyScreenTimesQuery.Period.DAILY,
        startedAt = date,
        endedAt = date,
        totalStayDuration = 15_600L,
        screenTimes =
            listOf(5_400L, 3_600L, 0L, 0L, 0L, 6_000L, 600L, 0L, 0L, 0L, 0L, 0L)
                .mapIndexed { index, stayDuration ->
                    GetMyScreenTimesResult.ScreenTime(
                        startedAt = date.atStartOfDay().plusHours(index * 2L),
                        endedAt = date.atStartOfDay().plusHours((index + 1) * 2L),
                        stayDuration = stayDuration
                    )
                }
    )

fun createGetMyWeeklyScreenTimesResult(): GetMyScreenTimesResult =
    GetMyScreenTimesResult(
        period = GetMyScreenTimesQuery.Period.WEEKLY,
        startedAt = LocalDate.parse("2026-02-08"),
        endedAt = LocalDate.parse("2026-02-14"),
        totalStayDuration = 19_800L,
        screenTimes =
            listOf(3_600L, 0L, 3_600L, 3_600L, 0L, 9_000L, 0L)
                .mapIndexed { index, stayDuration ->
                    GetMyScreenTimesResult.ScreenTime(
                        startedAt = LocalDate.parse("2026-02-08").atStartOfDay().plusDays(index.toLong()),
                        endedAt = LocalDate.parse("2026-02-08").atStartOfDay().plusDays((index + 1).toLong()),
                        stayDuration = stayDuration
                    )
                }
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
    isClosed: Boolean = true,
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
        isClosed = isClosed,
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
    isClosed: Boolean = true,
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
        isClosed = isClosed,
        scrollDepth = scrollDepth
    )
