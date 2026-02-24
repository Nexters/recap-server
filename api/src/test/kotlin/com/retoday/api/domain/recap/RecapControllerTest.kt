package com.retoday.api.domain.recap

import com.ninjasquad.springmockk.MockkBean
import com.retoday.api.common.ControllerTest
import com.retoday.api.domain.recap.controller.RecapController
import com.retoday.api.extension.document
import com.retoday.api.extension.expectBody
import com.retoday.api.extension.expectStatus
import com.retoday.api.extension.withAuthentication
import com.retoday.api.snippet.generateRecapQueryFields
import com.retoday.api.snippet.recapDetailResponseFields
import com.retoday.core.domain.recap.dto.response.RecapDetailResponse
import com.retoday.core.domain.recap.service.RecapService
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import java.time.LocalDate
import java.time.LocalDateTime

@WebMvcTest(RecapController::class)
class RecapControllerTest : ControllerTest() {
    @MockkBean
    private lateinit var recapService: RecapService

    init {
        describe("generateDailyRecap()은") {
            context("날짜 없이 유효한 요청이 주어진 경우") {
                val yesterday = LocalDate.now().minusDays(1)
                val result = createRecapDetailResponse(yesterday)

                every { recapService.generateDailyRecap(any(), null) } returns result

                it("상태 코드 200과 RecapDetailResponse를 반환한다.") {
                    webClient
                        .get()
                        .uri("/recaps/generate")
                        .withAuthentication()
                        .exchange()
                        .expectStatus(200)
                        .expectBody(result)
                        .document("리캡 생성 성공(200) - 기본 날짜") {
                            responseBody(recapDetailResponseFields)
                        }
                }
            }

            context("날짜가 포함된 유효한 요청이 주어진 경우") {
                val targetDate = LocalDate.parse("2026-02-21")
                val result = createRecapDetailResponse(targetDate)

                every { recapService.generateDailyRecap(any(), targetDate) } returns result

                it("상태 코드 200과 RecapDetailResponse를 반환한다.") {
                    webClient
                        .get()
                        .uri("/recaps/generate?date=$targetDate")
                        .withAuthentication()
                        .exchange()
                        .expectStatus(200)
                        .expectBody(result)
                        .document("리캡 생성 성공(200)") {
                            queryParams(generateRecapQueryFields)
                            responseBody(recapDetailResponseFields)
                        }
                }
            }

            context("리캡 생성을 위한 데이터가 없는 경우") {
                val targetDate = LocalDate.parse("2026-02-21")
                every { recapService.generateDailyRecap(any(), targetDate) } returns null

                it("상태 코드 204를 반환한다.") {
                    webClient
                        .get()
                        .uri("/recaps/generate?date=$targetDate")
                        .withAuthentication()
                        .exchange()
                        .expectStatus(204)
                }
            }
        }
    }

    private fun createRecapDetailResponse(date: LocalDate): RecapDetailResponse =
        RecapDetailResponse(
            id = 100L,
            userId = 1L,
            recapDate = date,
            title = "오늘의 리캡",
            summary = "요약 내용입니다.",
            startedAt = LocalDateTime.parse("2026-02-21T09:00:00"),
            closedAt = LocalDateTime.parse("2026-02-21T18:00:00"),
            sections = listOf(RecapDetailResponse.SectionResponse("섹션 제목", "섹션 내용")),
            timelines = listOf(RecapDetailResponse.TimelineResponse("09:00", "10:30", "집중 작업", 90)),
            topics = listOf(RecapDetailResponse.TopicResponse("개발", "핵심 주제", "토픽 내용"))
        )
}
