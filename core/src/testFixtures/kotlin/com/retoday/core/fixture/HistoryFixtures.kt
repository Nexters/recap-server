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
            listOf(
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date,
                    stayDuration = 5_400L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date,
                    stayDuration = 3_600L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date,
                    stayDuration = 0L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date,
                    stayDuration = 0L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date,
                    stayDuration = 0L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date,
                    stayDuration = 6_000L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date,
                    stayDuration = 600L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date,
                    stayDuration = 0L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date,
                    stayDuration = 0L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date,
                    stayDuration = 0L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date,
                    stayDuration = 0L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = date,
                    endedAt = date.plusDays(1),
                    stayDuration = 0L
                )
            )
    )

fun createGetMyWeeklyScreenTimesResult(): GetMyScreenTimesResult =
    GetMyScreenTimesResult(
        period = GetMyScreenTimesQuery.Period.WEEKLY,
        startedAt = LocalDate.parse("2026-02-08"),
        endedAt = LocalDate.parse("2026-02-14"),
        totalStayDuration = 19_800L,
        screenTimes =
            listOf(
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = LocalDate.parse("2026-02-08"),
                    endedAt = LocalDate.parse("2026-02-09"),
                    stayDuration = 3_600L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = LocalDate.parse("2026-02-09"),
                    endedAt = LocalDate.parse("2026-02-10"),
                    stayDuration = 0L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = LocalDate.parse("2026-02-10"),
                    endedAt = LocalDate.parse("2026-02-11"),
                    stayDuration = 3_600L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = LocalDate.parse("2026-02-11"),
                    endedAt = LocalDate.parse("2026-02-12"),
                    stayDuration = 3_600L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = LocalDate.parse("2026-02-12"),
                    endedAt = LocalDate.parse("2026-02-13"),
                    stayDuration = 0L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = LocalDate.parse("2026-02-13"),
                    endedAt = LocalDate.parse("2026-02-14"),
                    stayDuration = 9_000L
                ),
                GetMyScreenTimesResult.ScreenTime(
                    startedAt = LocalDate.parse("2026-02-14"),
                    endedAt = LocalDate.parse("2026-02-15"),
                    stayDuration = 0L
                )
            )
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
