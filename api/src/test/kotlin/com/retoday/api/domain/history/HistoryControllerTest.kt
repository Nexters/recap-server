package com.retoday.api.domain.history

import com.ninjasquad.springmockk.MockkBean
import com.retoday.api.common.ControllerTest
import com.retoday.api.domain.history.controller.HistoryController
import com.retoday.api.domain.history.dto.response.*
import com.retoday.api.domain.history.dto.response.GetMyCategoryAnalysesResponse
import com.retoday.api.domain.history.dto.response.GetMyLongestStayedWebsiteResponse
import com.retoday.api.domain.history.dto.response.GetMyScreenTimesResponse
import com.retoday.api.domain.history.dto.response.HistoryRecordResponse
import com.retoday.api.extension.*
import com.retoday.api.fixture.HISTORY_DOMAIN
import com.retoday.api.fixture.HISTORY_URL
import com.retoday.api.fixture.createHistoryRecordRequest
import com.retoday.api.snippet.*
import com.retoday.core.domain.history.dto.query.GetMyScreenTimesQuery
import com.retoday.core.domain.history.exception.*
import com.retoday.core.domain.history.service.HistoryService
import com.retoday.core.fixture.*
import com.retoday.core.fixture.createGetMyCategoryAnalysisResult
import com.retoday.core.fixture.createGetMyLongestStayedWebsiteResult
import com.retoday.core.fixture.createGetMyScreenTimesResult
import com.retoday.core.fixture.createHistoryRecordResult
import com.retoday.core.global.ratelimit.RateLimiter
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import java.time.Instant
import java.time.LocalDate

@WebMvcTest(HistoryController::class)
class HistoryControllerTest : ControllerTest() {
    @MockkBean
    private lateinit var historyService: HistoryService

    @MockkBean
    private lateinit var rateLimiter: RateLimiter

    init {
        describe("recordHistory()는") {
            val baseRequest = {
                webClient
                    .post()
                    .uri("/histories")
            }
            val authenticatedRequest = { request: Any ->
                baseRequest()
                    .bodyValue(request)
                    .withAuthentication()
                    .exchange()
            }

            context("유효한 요청이 주어진 경우") {
                every { rateLimiter.checkHistoryExceeded(any()) } returns null

                val result = createHistoryRecordResult()

                every { historyService.recordHistory(any(), any()) } returns result

                it("상태 코드 200과 HistoryRecordResponse를 반환한다.") {
                    authenticatedRequest(createHistoryRecordRequest())
                        .expectStatus(200)
                        .expectBody(HistoryRecordResponse.from(result))
                        .document("방문 기록 저장 성공(200)") {
                            requestBody(historyRecordRequestFields)
                            responseBody(historyRecordResponseFields)
                        }
                }
            }

            context("사용자가 제외한 도메인인 경우") {
                every { rateLimiter.checkHistoryExceeded(any()) } returns null

                every {
                    historyService.recordHistory(any(), any())
                } throws WebsiteExcludedByUserException(HISTORY_DOMAIN)

                it("상태 코드 204와 ErrorResponse를 반환한다.") {
                    authenticatedRequest(createHistoryRecordRequest())
                        .expectStatus(204)
                        .expectError()
                        .document("방문 기록 저장 실패(204)") {
                            responseBody(errorResponseFields)
                        }
                }
            }

            context("유효하지 않은 URL이 주어진 경우") {
                every { rateLimiter.checkHistoryExceeded(any()) } returns null

                every {
                    historyService.recordHistory(any(), any())
                } throws InvalidUrlException("invalid-url")

                it("상태 코드 400과 ErrorResponse를 반환한다.") {
                    authenticatedRequest(createHistoryRecordRequest())
                        .expectStatus(400)
                        .expectError()
                        .document("방문 기록 저장 실패 - 유효하지 않은 URL(400)") {
                            requestBody(historyRecordRequestFields)
                            responseBody(errorResponseFields)
                        }
                }
            }

            context("중복된 요청이 주어진 경우") {
                every { rateLimiter.checkHistoryExceeded(any()) } returns null

                every {
                    historyService.recordHistory(any(), any())
                } throws DuplicateHistoryException(1, HISTORY_URL)

                it("상태 코드 409와 ErrorResponse를 반환한다.") {
                    authenticatedRequest(createHistoryRecordRequest())
                        .expectStatus(409)
                        .expectError()
                        .document("방문 기록 저장 실패(409)") {
                            requestBody(historyRecordRequestFields)
                            responseBody(errorResponseFields)
                        }
                }
            }

            context("Rate Limit을 초과한 경우") {
                every { rateLimiter.checkHistoryExceeded(any()) } returns 60L

                every {
                    historyService.recordHistory(any(), any())
                } throws RateLimitExceededException(1L, 60L)

                it("상태 코드 429와 ErrorResponse를 반환한다.") {
                    authenticatedRequest(createHistoryRecordRequest())
                        .expectStatus(429)
                        .expectError()
                        .document("방문 기록 저장 실패(429)") {
                            requestBody(historyRecordRequestFields)
                            responseBody(errorResponseFields)
                        }
                }
            }

            context("Validation 실패 (closedAt < visitedAt)인 경우") {
                every { rateLimiter.checkHistoryExceeded(any()) } returns null

                val now = Instant.now()

                every {
                    historyService.recordHistory(any(), any())
                } throws InvalidTimeRangeException("closedAt은 visitedAt보다 이후여야 합니다")

                it("상태 코드 400과 ErrorResponse를 반환한다.") {
                    authenticatedRequest(
                        createHistoryRecordRequest(
                            visitedAt = now,
                            closedAt = now.minusSeconds(10)
                        )
                    ).expectStatus(400)
                        .expectError()
                        .document("방문 기록 저장 실패 - 유효하지 않은 시간 범위(400)") {
                            responseBody(errorResponseFields)
                        }
                }
            }
        }

        describe("getMyScreenTimes()은") {
            val date = LocalDate.parse("2026-02-13")
            val request =
                webClient
                    .get()
                    .uri("/users/me/screen-times?date=$date&period=${GetMyScreenTimesQuery.Period.DAILY}")
                    .withAuthentication()

            context("유효한 요청이 주어진 경우") {
                val result = createGetMyScreenTimesResult(date = date)
                every { historyService.getMyScreenTimes(any(), any()) } returns result

                it("상태 코드 200과 GetMyScreenTimesResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(200)
                        .expectBody(GetMyScreenTimesResponse.from(result))
                        .document("내 스크린타임 조회 성공(200)") {
                            queryParams(getMyScreenTimesQueryFields)
                            responseBody(getMyScreenTimesResponseFields)
                        }
                }
            }
        }

        describe("getMyCategoryAnalyses()은") {
            val date = LocalDate.parse("2026-02-13")
            val request =
                webClient
                    .get()
                    .uri("/users/me/category-analyses?date=$date")
                    .withAuthentication()

            context("유효한 요청이 주어진 경우") {
                val result = createGetMyCategoryAnalysisResult(date = date)
                every { historyService.getMyCategoryAnalyses(any(), any()) } returns result

                it("상태 코드 200과 GetMyCategoryAnalysisResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(200)
                        .expectBody(GetMyCategoryAnalysesResponse.from(result))
                        .document("내 카테고리 분석 조회 성공(200)") {
                            queryParams(getMyCategoryAnalysisQueryFields)
                            responseBody(getMyCategoryAnalysesResponseFields)
                        }
                }
            }
        }

        describe("getMyFrequentlyVisitedWebsites()은") {
            val date = LocalDate.parse("2026-02-13")
            val request =
                webClient
                    .get()
                    .uri("/users/me/frequently-visited-websites?date=$date&limit=3")
                    .withAuthentication()

            context("유효한 요청이 주어진 경우") {
                val result = createGetMyFrequentlyVisitedWebsitesResult(date = date)
                every { historyService.getMyFrequentlyVisitedWebsites(any(), any()) } returns result

                it("상태 코드 200과 GetMyFrequentlyVisitedWebsitesResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(200)
                        .expectBody(GetMyFrequentlyVisitedWebsitesResponse.from(result))
                        .document("자주 방문한 웹사이트 조회 성공(200)") {
                            queryParams(getMyFrequentlyVisitedWebsitesQueryFields)
                            responseBody(getMyFrequentlyVisitedWebsitesResponseFields)
                        }
                }
            }
        }

        describe("getMyWorkPattern()은") {
            val date = LocalDate.parse("2026-02-13")
            val request =
                webClient
                    .get()
                    .uri("/users/me/work-pattern?date=$date")
                    .withAuthentication()

            context("유효한 요청이 주어진 경우") {
                val result = createGetMyWorkPatternResult(date = date)
                every { historyService.getMyWorkPattern(any(), any()) } returns result

                it("상태 코드 200과 GetMyWorkPatternResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(200)
                        .expectBody(GetMyWorkPatternResponse.from(result))
                        .document("내 일간 작업 패턴 분석 조회 성공(200)") {
                            queryParams(getMyWorkPatternQueryFields)
                            responseBody(getMyWorkPatternResponseFields)
                        }
                }
            }
        }

        describe("getMyLongestStayedWebsite()은") {
            val date = LocalDate.parse("2026-02-13")
            val request =
                webClient
                    .get()
                    .uri("/users/me/longest-stayed-website?date=$date")
                    .withAuthentication()

            context("유효한 요청이 주어진 경우") {
                val result = createGetMyLongestStayedWebsiteResult(date = date)
                every { historyService.getMyLongestStayedWebsite(any(), any()) } returns result

                it("상태 코드 200과 GetMyLongestStayedWebsiteResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(200)
                        .expectBody(GetMyLongestStayedWebsiteResponse.from(result))
                        .document("내 최장 체류 웹사이트 조회 성공(200)") {
                            queryParams(getMyLongestStayedWebsiteQueryFields)
                            responseBody(getMyLongestStayedWebsiteResponseFields)
                        }
                }
            }
        }
    }
}
