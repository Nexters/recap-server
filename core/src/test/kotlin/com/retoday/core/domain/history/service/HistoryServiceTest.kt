package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.exception.DuplicateHistoryException
import com.retoday.core.domain.history.exception.InvalidTimeRangeException
import com.retoday.core.domain.history.exception.InvalidUrlException
import com.retoday.core.domain.history.repository.HistoryRepository
import com.retoday.core.fixture.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Instant

class HistoryServiceTest :
    BehaviorSpec({
        val historyRepository = mockk<HistoryRepository>()
        val websiteService = mockk<WebsiteService>()
        val pageService = mockk<PageService>()
        val historyService =
            HistoryService(
                historyRepository,
                websiteService,
                pageService
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
    })
