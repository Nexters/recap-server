package com.retoday.core.domain.history.repository

import com.retoday.core.common.RepositoryTest
import com.retoday.core.fixture.*
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.time.LocalDate

class HistoryRepositoryTest : RepositoryTest() {
    @Autowired
    private lateinit var historyRepository: HistoryRepository

    init {
        "findWebsiteStatsWithCategoryByUserId()" {
            val userId = 1L
            val periodStartedAt = Instant.parse("2026-02-13T00:00:00Z")
            val periodEndedAt = Instant.parse("2026-02-14T00:00:00Z")

            val category = createWebsiteCategory(id = null, name = "개발").save()
            val github = createWebsite(id = null, domain = DOMAIN, categoryId = category.id).save()
            val news = createWebsite(id = null, domain = USER_EX_DOMAIN, faviconUrl = null).save()
            val githubId = github.id!!
            val newsId = news.id!!

            createHistory(
                id = null,
                userId = userId,
                websiteId = githubId,
                pageId = 11L,
                visitedAt = Instant.parse("2026-02-12T23:30:00Z"),
                closedAt = Instant.parse("2026-02-13T00:30:00Z"),
                stayDuration = 3_600,
                visitedDate = LocalDate.parse("2026-02-12"),
                visitedHour = 23
            ).save()
            createHistory(
                id = null,
                userId = userId,
                websiteId = githubId,
                pageId = 12L,
                visitedAt = Instant.parse("2026-02-13T01:00:00Z"),
                closedAt = Instant.parse("2026-02-13T01:30:00Z"),
                stayDuration = 1_800,
                visitedDate = LocalDate.parse("2026-02-13"),
                visitedHour = 1
            ).save()
            createHistory(
                id = null,
                userId = userId,
                websiteId = newsId,
                pageId = 13L,
                visitedAt = Instant.parse("2026-02-13T23:30:00Z"),
                closedAt = Instant.parse("2026-02-14T00:30:00Z"),
                stayDuration = 3_600,
                visitedDate = LocalDate.parse("2026-02-13"),
                visitedHour = 23
            ).save()
            createHistory(
                id = null,
                userId = userId,
                websiteId = newsId,
                pageId = 14L,
                visitedAt = Instant.parse("2026-02-14T01:00:00Z"),
                closedAt = Instant.parse("2026-02-14T02:00:00Z"),
                stayDuration = 3_600,
                visitedDate = LocalDate.parse("2026-02-14"),
                visitedHour = 1
            ).save()
            createHistory(
                id = null,
                userId = 2L,
                websiteId = githubId,
                pageId = 15L,
                visitedAt = Instant.parse("2026-02-13T02:00:00Z"),
                closedAt = Instant.parse("2026-02-13T03:00:00Z"),
                stayDuration = 3_600,
                visitedDate = LocalDate.parse("2026-02-13"),
                visitedHour = 2
            ).save()

            entityManager.flush()
            entityManager.clear()

            val analyses =
                historyRepository.findWebsiteStatsWithCategoryByUserId(
                    userId = userId,
                    startedAt = periodStartedAt,
                    endedAt = periodEndedAt
                )

            analyses shouldHaveSize 2

            val githubAnalysis = analyses.first { it.domain == DOMAIN }
            githubAnalysis.categoryName shouldBe "개발"
            githubAnalysis.faviconUrl shouldBe github.faviconUrl
            githubAnalysis.stayDuration shouldBe 3_600L

            val newsAnalysis = analyses.first { it.domain == USER_EX_DOMAIN }
            newsAnalysis.faviconUrl shouldBe null
            newsAnalysis.categoryName shouldBe null
            newsAnalysis.stayDuration shouldBe 1_800L
        }
    }
}
