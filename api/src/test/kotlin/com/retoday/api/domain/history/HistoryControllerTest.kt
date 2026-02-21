package com.retoday.api.domain.history

import com.ninjasquad.springmockk.MockkBean
import com.retoday.api.common.ControllerTest
import com.retoday.api.domain.history.controller.HistoryController
import com.retoday.api.domain.history.dto.response.GetMyScreenTimesResponse
import com.retoday.api.domain.history.dto.response.HistoryRecordResponse
import com.retoday.api.extension.*
import com.retoday.api.fixture.createHistoryRecordRequest
import com.retoday.api.snippet.errorResponseFields
import com.retoday.api.snippet.getMyScreenTimesResponseFields
import com.retoday.api.snippet.historyRecordRequestFields
import com.retoday.api.snippet.historyRecordResponseFields
import com.retoday.core.domain.history.dto.command.HistoryRecordCommand
import com.retoday.core.domain.history.dto.query.GetMyScreenTimesQuery
import com.retoday.core.domain.history.exception.*
import com.retoday.core.domain.history.service.HistoryService
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
        beforeTest {
            every { rateLimiter.checkHistoryExceeded(any()) } returns null
        }

        describe("recordHistory()는") {
            val baseRequest = { webClient.post().uri("/histories") }
            val authenticatedRequest = { request: Any ->
                baseRequest()
                    .bodyValue(request)
                    .withAuthentication()
                    .exchange()
            }

            context("유효한 요청이 주어진 경우") {
                val result = createHistoryRecordResult()

                every {
                    historyService.recordHistory(any<Long>(), any<HistoryRecordCommand>())
                } returns result

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
                every {
                    historyService.recordHistory(any<Long>(), any<HistoryRecordCommand>())
                } throws WebsiteExcludedByUserException("github.com")

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
                every {
                    historyService.recordHistory(any<Long>(), any<HistoryRecordCommand>())
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
                every {
                    historyService.recordHistory(any<Long>(), any<HistoryRecordCommand>())
                } throws DuplicateHistoryException(1, "https://github.com/Nexters/retoday-server")

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
                val now = Instant.now()

                every {
                    historyService.recordHistory(any<Long>(), any<HistoryRecordCommand>())
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
                            queryParams(
                                "date" desc "조회 기준 일자(yyyy-MM-dd)",
                                "period" desc "조회 기간 타입(DAILY, WEEKLY)"
                            )
                            responseBody(getMyScreenTimesResponseFields)
                        }
                }
            }
        }
    }
}
