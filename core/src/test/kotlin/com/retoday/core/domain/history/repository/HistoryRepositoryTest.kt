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

        "findWebsiteStatsWithVisitCountByUserId()" {
            val userId = 1L
            val periodStartedAt = Instant.parse("2026-02-13T00:00:00Z")
            val periodEndedAt = Instant.parse("2026-02-14T00:00:00Z")
            val limit = 2

            val github = createWebsite(id = null, domain = DOMAIN).save()
            val youtube = createWebsite(id = null, domain = "youtube.com").save()
            val news = createWebsite(id = null, domain = USER_EX_DOMAIN).save()
            val githubId = github.id!!
            val youtubeId = youtube.id!!
            val newsId = news.id!!

            createHistory(
                id = null,
                userId = userId,
                websiteId = githubId,
                pageId = 21L,
                visitedAt = Instant.parse("2026-02-13T00:10:00Z"),
                closedAt = Instant.parse("2026-02-13T00:40:00Z"),
                stayDuration = 1_800,
                visitedDate = LocalDate.parse("2026-02-13"),
                visitedHour = 0
            ).save()
            createHistory(
                id = null,
                userId = userId,
                websiteId = githubId,
                pageId = 22L,
                visitedAt = Instant.parse("2026-02-13T01:00:00Z"),
                closedAt = Instant.parse("2026-02-13T01:20:00Z"),
                stayDuration = 1_200,
                visitedDate = LocalDate.parse("2026-02-13"),
                visitedHour = 1
            ).save()
            createHistory(
                id = null,
                userId = userId,
                websiteId = youtubeId,
                pageId = 23L,
                visitedAt = Instant.parse("2026-02-13T02:00:00Z"),
                closedAt = Instant.parse("2026-02-13T03:00:00Z"),
                stayDuration = 3_600,
                visitedDate = LocalDate.parse("2026-02-13"),
                visitedHour = 2
            ).save()
            createHistory(
                id = null,
                userId = userId,
                websiteId = youtubeId,
                pageId = 24L,
                visitedAt = Instant.parse("2026-02-13T23:50:00Z"),
                closedAt = Instant.parse("2026-02-14T00:10:00Z"),
                stayDuration = 1_200,
                visitedDate = LocalDate.parse("2026-02-13"),
                visitedHour = 23
            ).save()
            createHistory(
                id = null,
                userId = userId,
                websiteId = newsId,
                pageId = 25L,
                visitedAt = Instant.parse("2026-02-12T23:50:00Z"),
                closedAt = Instant.parse("2026-02-13T00:20:00Z"),
                stayDuration = 1_800,
                visitedDate = LocalDate.parse("2026-02-12"),
                visitedHour = 23
            ).save()
            createHistory(
                id = null,
                userId = userId,
                websiteId = newsId,
                pageId = 26L,
                visitedAt = Instant.parse("2026-02-13T05:00:00Z"),
                closedAt = Instant.parse("2026-02-13T05:10:00Z"),
                stayDuration = 600,
                visitedDate = LocalDate.parse("2026-02-13"),
                visitedHour = 5
            ).save()
            createHistory(
                id = null,
                userId = 2L,
                websiteId = githubId,
                pageId = 27L,
                visitedAt = Instant.parse("2026-02-13T08:00:00Z"),
                closedAt = Instant.parse("2026-02-13T09:00:00Z"),
                stayDuration = 3_600,
                visitedDate = LocalDate.parse("2026-02-13"),
                visitedHour = 8
            ).save()

            entityManager.flush()
            entityManager.clear()

            val analyses =
                historyRepository.findWebsiteStatsWithVisitCountByUserId(
                    userId = userId,
                    startedAt = periodStartedAt,
                    endedAt = periodEndedAt,
                    limit = limit
                )

            analyses shouldHaveSize 2

            analyses[0].domain shouldBe "youtube.com"
            analyses[0].visitCount shouldBe 2L
            analyses[0].stayDuration shouldBe 4_200L

            analyses[1].domain shouldBe DOMAIN
            analyses[1].visitCount shouldBe 2L
            analyses[1].stayDuration shouldBe 3_000L
        }
    }
}
