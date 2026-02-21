package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.client.AICategoryClient
import com.retoday.core.domain.history.dto.projection.WebsiteStat
import com.retoday.core.domain.history.dto.projection.WorkPatternHourlyCount
import com.retoday.core.domain.history.dto.query.GetMyCategoryAnalysisQuery
import com.retoday.core.domain.history.dto.query.GetMyFrequentlyVisitedWebsitesQuery
import com.retoday.core.domain.history.dto.query.GetMyLongestStayedWebsiteQuery
import com.retoday.core.domain.history.dto.query.GetMyScreenTimesQuery
import com.retoday.core.domain.history.dto.query.GetMyWorkPatternQuery
import com.retoday.core.domain.history.entity.Website
import com.retoday.core.domain.history.entity.WebsiteCategory
import com.retoday.core.domain.history.exception.DuplicateHistoryException
import com.retoday.core.domain.history.exception.InvalidCategoryException
import com.retoday.core.domain.history.exception.InvalidTimeRangeException
import com.retoday.core.domain.history.exception.InvalidUrlException
import com.retoday.core.domain.history.repository.HistoryRepository
import com.retoday.core.domain.history.repository.WebsiteCategoryRepository
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.fixture.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class HistoryServiceTest :
    BehaviorSpec({
        val historyRepository = mockk<HistoryRepository>()
        val websiteService = mockk<WebsiteService>()
        val pageService = mockk<PageService>()
        val profileRepository = mockk<ProfileRepository>()
        val categoryRepository = mockk<WebsiteCategoryRepository>()
        val aiClient = mockk<AICategoryClient>()
        val historyService =
            HistoryService(
                historyRepository = historyRepository,
                profileRepository = profileRepository,
                websiteService = websiteService,
                pageService = pageService,
                categoryRepository = categoryRepository,
                aiClient = aiClient
            )

        val userId = ID
        val website = createWebsite()
        val page = createPage()
        val history = createHistory()

        fun setupSuccessfulRecordMocks(faviconUrl: String? = FAVICON_URL) {
            every { websiteService.findOrCreate(any(), faviconUrl) } returns website
            every { pageService.findOrCreate(any(), any(), any(), any()) } returns page
            every { historyRepository.findByUserIdAndPageIdAndVisitedAtAfter(any(), any(), any()) } returns null
            every { historyRepository.save(any()) } returns history
        }

        Given("사용자가 페이지를 방문했을 때") {
            val command = createHistoryRecordCommand()

            setupSuccessfulRecordMocks()

            When("히스토리 기록을 요청하면") {
                val result = historyService.recordHistory(userId, command)

                Then("방문 기록이 저장되고 결과가 반환된다") {
                    result.historyId shouldBe history.id
                    result.pageId shouldBe page.id
                    result.websiteId shouldBe website.id
                    result.stayDuration shouldBe 10

                    verify(exactly = 1) { websiteService.findOrCreate(DOMAIN, FAVICON_URL) }
                    verify(exactly = 1) { pageService.findOrCreate(any(), any(), any(), any()) }
                    verify(exactly = 1) { historyRepository.save(any()) }
                }
            }
        }

        Given("사용자가 이미 최근에 방문한 페이지에") {
            val command =
                createHistoryRecordCommand(
                    title = null,
                    description = null,
                    faviconUrl = null,
                    isClosed = false
                )

            setupSuccessfulRecordMocks(faviconUrl = null)
            every {
                historyRepository.findByUserIdAndPageIdAndVisitedAtAfter(any(), any(), any())
            } returns createHistory()

            When("다시 히스토리 기록을 요청하면") {
                Then("중복 기록으로 거부된다") {
                    shouldThrow<DuplicateHistoryException> {
                        historyService.recordHistory(userId, command)
                    }
                }
            }
        }

        Given("유효하지 않은 URL로") {
            val command = createHistoryRecordCommand(url = "invalid-url")

            When("히스토리 기록을 요청하면") {
                Then("요청이 거부된다") {
                    shouldThrow<InvalidUrlException> {
                        historyService.recordHistory(userId, command)
                    }
                }
            }
        }

        Given("시간 범위가 잘못된 방문 기록으로") {
            val now = Instant.now()
            val command =
                createHistoryRecordCommand(
                    visitedAt = now,
                    closedAt = now.minusSeconds(10)
                )

            When("히스토리 기록을 요청하면") {
                Then("요청이 거부된다") {
                    shouldThrow<InvalidTimeRangeException> {
                        historyService.recordHistory(userId, command)
                    }
                }
            }
        }

        Given("사용자가 탭을 이동했을 때") {
            val command = createHistoryRecordCommand(isClosed = false)

            setupSuccessfulRecordMocks()

            When("히스토리 기록을 요청하면") {
                val result = historyService.recordHistory(userId, command)

                Then("방문 기록이 저장된다") {
                    result.historyId shouldBe history.id

                    verify(exactly = 1) { historyRepository.save(any()) }
                }
            }
        }

        Given("일간 스크린타임 집계가 필요할 때") {
            val targetDate = LocalDate.parse("2026-02-13")
            val query =
                GetMyScreenTimesQuery(
                    date = targetDate,
                    period = GetMyScreenTimesQuery.Period.DAILY
                )
            val profile = createProfile()
            val dayStartUtc = Instant.parse("2026-02-12T15:00:00Z")
            val dayEndUtc = Instant.parse("2026-02-13T15:00:00Z")
            every { profileRepository.findByUserId(userId) } returns profile
            every {
                historyRepository.findAllByUserIdAndVisitedAtBeforeAndClosedAtAfter(
                    userId = userId,
                    visitedAt = dayEndUtc,
                    closedAt = dayStartUtc
                )
            } returns
                listOf(
                    createHistory(
                        id = 1L,
                        websiteId = 101L,
                        visitedAt = Instant.parse("2026-02-12T15:30:00Z"),
                        closedAt = Instant.parse("2026-02-12T17:00:00Z"),
                        stayDuration = 5_400
                    ),
                    createHistory(
                        id = 2L,
                        websiteId = 102L,
                        visitedAt = Instant.parse("2026-02-12T17:30:00Z"),
                        closedAt = Instant.parse("2026-02-12T18:30:00Z"),
                        stayDuration = 3_600
                    ),
                    createHistory(
                        id = 3L,
                        websiteId = 103L,
                        visitedAt = Instant.parse("2026-02-13T01:00:00Z"),
                        closedAt = Instant.parse("2026-02-13T02:30:00Z"),
                        stayDuration = 5_400
                    ),
                    createHistory(
                        id = 4L,
                        websiteId = 103L,
                        visitedAt = Instant.parse("2026-02-13T02:50:00Z"),
                        closedAt = Instant.parse("2026-02-13T03:10:00Z"),
                        stayDuration = 1_200
                    )
                )

            When("사용자가 본인 일간 스크린타임을 조회하면") {
                val result = historyService.getMyScreenTimes(userId, query)

                Then("2시간 단위 버킷과 총 체류 시간이 정확하게 계산된다.") {
                    result shouldBe createGetMyScreenTimesResult(date = targetDate)
                    verify(exactly = 1) {
                        historyRepository.findAllByUserIdAndVisitedAtBeforeAndClosedAtAfter(
                            userId = userId,
                            visitedAt = dayEndUtc,
                            closedAt = dayStartUtc
                        )
                    }
                }
            }
        }

        Given("주간 스크린타임 집계가 필요할 때") {
            val targetDate = LocalDate.parse("2026-02-13")
            val query =
                GetMyScreenTimesQuery(
                    date = targetDate,
                    period = GetMyScreenTimesQuery.Period.WEEKLY
                )
            val profile = createProfile()
            val weekStartUtc = Instant.parse("2026-02-07T15:00:00Z")
            val weekEndUtc = Instant.parse("2026-02-14T15:00:00Z")
            every { profileRepository.findByUserId(userId) } returns profile
            every {
                historyRepository.findAllByUserIdAndVisitedAtBeforeAndClosedAtAfter(
                    userId = userId,
                    visitedAt = weekEndUtc,
                    closedAt = weekStartUtc
                )
            } returns
                listOf(
                    createHistory(
                        id = 11L,
                        websiteId = 201L,
                        visitedAt = Instant.parse("2026-02-07T15:30:00Z"),
                        closedAt = Instant.parse("2026-02-07T16:30:00Z"),
                        stayDuration = 3_600
                    ),
                    createHistory(
                        id = 12L,
                        websiteId = 202L,
                        visitedAt = Instant.parse("2026-02-10T14:00:00Z"),
                        closedAt = Instant.parse("2026-02-10T16:00:00Z"),
                        stayDuration = 7_200
                    ),
                    createHistory(
                        id = 13L,
                        websiteId = 203L,
                        visitedAt = Instant.parse("2026-02-13T01:00:00Z"),
                        closedAt = Instant.parse("2026-02-13T03:30:00Z"),
                        stayDuration = 9_000
                    )
                )

            When("사용자가 본인 주간 스크린타임을 조회하면") {
                val result = historyService.getMyScreenTimes(userId, query)

                Then("요일 단위 버킷과 총 체류 시간이 정확하게 계산된다.") {
                    result shouldBe createGetMyWeeklyScreenTimesResult()
                    verify(exactly = 1) {
                        historyRepository.findAllByUserIdAndVisitedAtBeforeAndClosedAtAfter(
                            userId = userId,
                            visitedAt = weekEndUtc,
                            closedAt = weekStartUtc
                        )
                    }
                }
            }
        }

        Given("웹사이트 도메인 카테고리 분류가 필요할 때") {
            val domain = "hackers.com"
            val studyCategory = WebsiteCategory(id = 10L, name = "학습")
            val categoryList = listOf(studyCategory)
            val categoryNames = listOf("학습")

            When("카테고리가 할당되지 않은 도메인인 경우") {
                val targetWebsite = Website(id = 1L, domain = domain, categoryId = null)
                every { categoryRepository.findAll() } returns categoryList
                every { aiClient.classify(domain, categoryNames) } returns "학습"
                every { categoryRepository.findByName("학습") } returns studyCategory

                historyService.classifyCategory(targetWebsite, domain)

                Then("AI 결과에 따라 웹사이트의 카테고리가 업데이트되어야 한다") {
                    targetWebsite.categoryId shouldBe 10L
                    verify(exactly = 1) { aiClient.classify(domain, any()) }
                }
            }

            When("AI가 분류한 카테고리가 DB에 존재하지 않는 이름이라면") {
                val freshWebsite = Website(id = 2L, domain = domain, categoryId = null)
                every { categoryRepository.findAll() } returns categoryList
                every { aiClient.classify(domain, categoryNames) } returns "잘못된카테고리"
                every { categoryRepository.findByName("잘못된카테고리") } returns null

                Then("InvalidCategoryException이 발생해야 한다") {
                    shouldThrow<InvalidCategoryException> {
                        historyService.classifyCategory(freshWebsite, domain)
                    }
                }
            }
        }

        Given("일간 카테고리 분석 집계가 필요할 때") {
            val targetDate = LocalDate.parse("2026-02-13")
            val query =
                GetMyCategoryAnalysisQuery(
                    date = targetDate
                )
            val expectedResult = createGetMyCategoryAnalysisResult(date = targetDate)
            val profile = createProfile()
            val dayStartUtc = Instant.parse("2026-02-12T15:00:00Z")
            val dayEndUtc = Instant.parse("2026-02-13T15:00:00Z")
            every { profileRepository.findByUserId(userId) } returns profile
            every {
                historyRepository.findWebsiteStatsWithCategoryByUserId(
                    userId = userId,
                    startedAt = dayStartUtc,
                    endedAt = dayEndUtc
                )
            } returns
                expectedResult.categoryAnalyses
                    .flatMap { categoryAnalysis ->
                        categoryAnalysis.websiteAnalyses.map { websiteAnalysis ->
                            createWebsiteStatWithCategory(
                                domain = websiteAnalysis.domain,
                                faviconUrl = websiteAnalysis.faviconUrl,
                                categoryName =
                                    if (categoryAnalysis.categoryName == "기타") {
                                        null
                                    } else {
                                        categoryAnalysis.categoryName
                                    },
                                stayDuration = websiteAnalysis.stayDuration
                            )
                        }
                    }

            When("사용자가 본인 일간 카테고리 분석을 조회하면") {
                val result = historyService.getMyCategoryAnalyses(userId, query)

                Then("카테고리별 체류 시간과 도메인 목록이 정확하게 계산된다.") {
                    result shouldBe expectedResult
                    verify(exactly = 1) {
                        historyRepository.findWebsiteStatsWithCategoryByUserId(
                            userId = userId,
                            startedAt = dayStartUtc,
                            endedAt = dayEndUtc
                        )
                    }
                }
            }
        }

        Given("특정 일자 자주 방문한 웹사이트 집계가 필요할 때") {
            val targetDate = LocalDate.parse("2026-02-13")
            val query =
                GetMyFrequentlyVisitedWebsitesQuery(
                    date = targetDate,
                    limit = 2
                )
            val profile = createProfile()
            val dayStartUtc =
                targetDate
                    .atStartOfDay(profile.timeZone.id)
                    .toInstant()
            val dayEndUtc = dayStartUtc.plus(1, ChronoUnit.DAYS)
            val expectedResult = createGetMyFrequentlyVisitedWebsitesResult(date = targetDate)

            every { profileRepository.findByUserId(userId) } returns profile
            every {
                historyRepository.findWebsiteStatsWithVisitCountByUserId(
                    userId = userId,
                    startedAt = dayStartUtc,
                    endedAt = dayEndUtc,
                    limit = query.limit
                )
            } returns
                expectedResult.websiteAnalyses.map {
                    createWebsiteStatWithVisitCount(
                        domain = it.domain,
                        faviconUrl = it.faviconUrl,
                        visitCount = it.visitCount,
                        stayDuration = it.stayDuration
                    )
                }

            When("사용자가 특정 일자의 자주 방문한 웹사이트를 조회하면") {
                val result = historyService.getMyFrequentlyVisitedWebsites(userId, query)

                Then("방문 횟수와 체류 시간 기준 목록이 반환된다.") {
                    result shouldBe expectedResult
                    verify(exactly = 1) {
                        historyRepository.findWebsiteStatsWithVisitCountByUserId(
                            userId = userId,
                            startedAt = dayStartUtc,
                            endedAt = dayEndUtc,
                            limit = query.limit
                        )
                    }
                }
            }
        }

        Given("일간 작업 패턴 분석 집계가 필요할 때") {
            val targetDate = LocalDate.parse("2026-02-13")
            val query = GetMyWorkPatternQuery(date = targetDate)
            val profile = createProfile()
            val dayStartUtc = Instant.parse("2026-02-12T15:00:00Z")
            val expectedResult =
                createGetMyWorkPatternResult(
                    date = targetDate,
                    dawnCount = 2L,
                    morningCount = 3L,
                    daytimeCount = 5L,
                    eveningCount = 4L
                )

            every { profileRepository.findByUserId(userId) } returns profile
            every {
                historyRepository.findHourlyHistoryCountsByUserId(
                    userId = userId,
                    startedAt = Instant.parse("2026-02-12T15:00:00Z")
                )
            } returns
                listOf(
                    WorkPatternHourlyCount(hour = 0L, count = 1L),
                    WorkPatternHourlyCount(hour = 5L, count = 1L),
                    WorkPatternHourlyCount(hour = 6L, count = 2L),
                    WorkPatternHourlyCount(hour = 11L, count = 1L),
                    WorkPatternHourlyCount(hour = 12L, count = 2L),
                    WorkPatternHourlyCount(hour = 15L, count = 3L),
                    WorkPatternHourlyCount(hour = 18L, count = 1L),
                    WorkPatternHourlyCount(hour = 23L, count = 3L)
                )

            When("사용자가 본인 일간 작업 패턴 분석을 조회하면") {
                val result = historyService.getMyWorkPattern(userId, query)

                Then("시간대별 활동 기록 개수가 정확하게 계산된다.") {
                    result shouldBe expectedResult
                    verify(exactly = 1) {
                        historyRepository.findHourlyHistoryCountsByUserId(
                            userId = userId,
                            startedAt = dayStartUtc
                        )
                    }
                }
            }
        }

        Given("가장 오래 머문 웹사이트 조회가 필요할 때") {
            val targetDate = LocalDate.parse("2026-02-13")
            val query = GetMyLongestStayedWebsiteQuery(date = targetDate)
            val profile = createProfile()
            val dayStartUtc = Instant.parse("2026-02-12T15:00:00Z")
            val dayEndUtc = Instant.parse("2026-02-13T15:00:00Z")
            val expectedResult = createGetMyLongestStayedWebsiteResult(date = targetDate, stayDuration = 5_400L)

            every { profileRepository.findByUserId(userId) } returns profile
            every {
                historyRepository.findTopWebsiteStatByUserId(
                    userId = userId,
                    startedAt = dayStartUtc,
                    endedAt = dayEndUtc
                )
            } returns
                WebsiteStat(
                    domain = expectedResult.domain!!,
                    faviconUrl = expectedResult.faviconUrl,
                    stayDuration = expectedResult.stayDuration
                )

            When("사용자가 본인의 최장 체류 웹사이트를 조회하면") {
                val result = historyService.getMyLongestStayedWebsite(userId, query)

                Then("최장 체류 웹사이트 정보가 반환된다.") {
                    result shouldBe expectedResult
                    verify(exactly = 1) {
                        historyRepository.findTopWebsiteStatByUserId(
                            userId = userId,
                            startedAt = dayStartUtc,
                            endedAt = dayEndUtc
                        )
                    }
                }
            }
        }
    })
