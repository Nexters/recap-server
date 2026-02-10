package com.retoday.api.domain.history

import com.ninjasquad.springmockk.MockkBean
import com.retoday.api.common.ControllerTest
import com.retoday.api.domain.history.controller.HistoryController
import com.retoday.api.domain.history.dto.response.HistoryRecordBatchResponse
import com.retoday.api.domain.history.dto.response.HistoryRecordResponse
import com.retoday.api.extension.*
import com.retoday.api.fixture.createHistoryRecordBatchRequest
import com.retoday.api.fixture.createHistoryRecordRequest
import com.retoday.api.snippet.*
import com.retoday.core.domain.history.dto.command.HistoryRecordBatchCommand
import com.retoday.core.domain.history.dto.command.HistoryRecordCommand
import com.retoday.core.domain.history.exception.DuplicateHistoryException
import com.retoday.core.domain.history.exception.InvalidTimeRangeException
import com.retoday.core.domain.history.exception.InvalidUrlException
import com.retoday.core.domain.history.exception.RateLimitExceededException
import com.retoday.core.domain.history.exception.WebsiteExcludedByUserException
import com.retoday.core.domain.history.service.HistoryService
import com.retoday.core.fixture.createHistoryRecordBatchResult
import com.retoday.core.fixture.createHistoryRecordBatchResultWithFailures
import com.retoday.core.fixture.createHistoryRecordResult
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import java.time.Instant

@WebMvcTest(HistoryController::class)
class HistoryControllerTest : ControllerTest() {
    @MockkBean
    private lateinit var historyService: HistoryService

    init {
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

                it("상태 코드 201과 HistoryRecordResponse를 반환한다.") {
                    authenticatedRequest(createHistoryRecordRequest())
                        .expectStatus(201)
                        .expectBody(HistoryRecordResponse.from(result))
                        .document("방문 기록 저장 성공(201)") {
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
                every {
                    historyService.recordHistory(any<Long>(), any<HistoryRecordCommand>())
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

        describe("recordHistoryBatch()는") {
            val batchRequest = { webClient.post().uri("/histories/batch") }
            val authenticatedBatchRequest = { request: Any ->
                batchRequest()
                    .bodyValue(request)
                    .withAuthentication()
                    .exchange()
            }

            context("유효한 배치 요청이 주어진 경우") {
                val result = createHistoryRecordBatchResult()

                every {
                    historyService.recordHistoryBatch(any<Long>(), any<HistoryRecordBatchCommand>())
                } returns result

                it("상태 코드 201과 BatchResponse를 반환한다.") {
                    authenticatedBatchRequest(createHistoryRecordBatchRequest())
                        .expectStatus(201)
                        .expectBody(HistoryRecordBatchResponse.from(result))
                        .document("방문 기록 일괄 저장 성공(201)") {
                            requestBody(historyRecordBatchRequestFields)
                            responseBody(historyRecordBatchResponseFields)
                        }
                }
            }

            context("일부 요청이 실패한 경우") {
                val result = createHistoryRecordBatchResultWithFailures()

                every {
                    historyService.recordHistoryBatch(any<Long>(), any<HistoryRecordBatchCommand>())
                } returns result

                it("상태 코드 201과 부분 실패 응답을 반환한다.") {
                    authenticatedBatchRequest(createHistoryRecordBatchRequest())
                        .expectStatus(201)
                        .expectBody(HistoryRecordBatchResponse.from(result))
                        .document(
                            "방문 기록 일괄 저장 부분 실패(201)",
                            nullableFields =
                                setOf(
                                    "results[].historyId",
                                    "results[].errorCode",
                                    "results[].errorMessage"
                                )
                        ) {
                            requestBody(historyRecordBatchRequestFields)
                            responseBody(historyRecordBatchResponseFields)
                        }
                }
            }

            context("최대 개수(100개)를 초과한 경우") {
                it("상태 코드 400과 ErrorResponse를 반환한다.") {
                    authenticatedBatchRequest(
                        createHistoryRecordBatchRequest(
                            records =
                                (1..101).map {
                                    createHistoryRecordRequest(tabId = it)
                                }
                        )
                    ).expectStatus(400)
                        .expectError()
                        .document("방문 기록 일괄 저장 실패(400)") {
                            responseBody(errorResponseFields)
                        }
                }
            }
        }
    }
}
