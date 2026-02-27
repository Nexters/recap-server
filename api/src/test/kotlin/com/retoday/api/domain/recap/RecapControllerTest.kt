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
import com.retoday.core.domain.recap.entity.RecapStatus
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
        describe("getDailyRecap()은") {
            context("날짜 없이 유효한 요청이 주어진 경우") {
                val yesterday = LocalDate.now().minusDays(1)
                val result = createRecapDetailResponse(yesterday)

                every { recapService.getDailyRecap(any(), null) } returns result

                it("상태 코드 200과 RecapDetailResponse를 반환한다.") {
                    webClient
                        .get()
                        .uri("/recaps")
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

                every { recapService.getDailyRecap(any(), targetDate) } returns result

                it("상태 코드 200과 RecapDetailResponse를 반환한다.") {
                    webClient
                        .get()
                        .uri("/recaps?date=$targetDate")
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
                every { recapService.getDailyRecap(any(), targetDate) } returns null

                it("상태 코드 204를 반환한다.") {
                    webClient
                        .get()
                        .uri("/recaps?date=$targetDate")
                        .withAuthentication()
                        .exchange()
                        .expectStatus(204)
                }
            }
        }

        describe("generateDailyRecap()은") {
            context("유효한 요청이 주어진 경우") {
                val targetDate = LocalDate.parse("2026-02-21")
                val result = createRecapDetailResponse(targetDate)

                every { recapService.generateDailyRecap(any(), targetDate) } returns result

                it("POST 요청에 대해 상태 코드 200과 RecapDetailResponse를 반환한다.") {
                    webClient
                        .post()
                        .uri("/recaps/generate?date=$targetDate")
                        .withAuthentication()
                        .exchange()
                        .expectStatus(200)
                        .expectBody(result)
                }
            }
        }
    }

    private fun createRecapDetailResponse(date: LocalDate): RecapDetailResponse =
        RecapDetailResponse(
            id = 100L,
            userId = 1L,
            recapDate = date,
            status = RecapStatus.COMPLETED,
            title = "집중적인 연구의 하루",
            summary = "취업준비와 개발공부를 병행하며 열심히 앞으로 나아갔어요. 앞으로도 꾸준히 작업하다보면 원하는 결과를 얻을 수 있을거에요!",
            startedAt = LocalDateTime.parse("2026-02-21T09:00:00"),
            closedAt = LocalDateTime.parse("2026-02-21T22:30:00"),
            sections =
                listOf(
                    RecapDetailResponse.SectionResponse(
                        "개발하며 고군분투한 하루",
                        "오늘은 주로 개발과 학습에 집중하셨네요. 오전 10시부터 오후 3시까지 가장 활발한 활동을 보였으며, 오늘은 주로 개발과 학습에 집중하셨네요."
                    ),
                    RecapDetailResponse.SectionResponse(
                        "업무 효율화를 위한 여정",
                        "AI와 피그마를 연결하거나, 디자인 시스템 정비를 위한 MCP(Model Context Protocol) 활용법 등 최신 AI 툴을 업무에 녹여내려 노력했어요."
                    )
                ),
            timelines =
                listOf(
                    RecapDetailResponse.TimelineResponse("09:00", "10:30", "신발 쇼핑하기", 90),
                    RecapDetailResponse.TimelineResponse("11:00", "11:30", "스포츠 뉴스 보기", 30),
                    RecapDetailResponse.TimelineResponse("12:15", "13:30", "신발 쇼핑하기", 75),
                    RecapDetailResponse.TimelineResponse("14:00", "15:30", "신발 쇼핑하기", 90),
                    RecapDetailResponse.TimelineResponse("16:00", "17:30", "신발 쇼핑하기", 90),
                    RecapDetailResponse.TimelineResponse("18:00", "19:30", "신발 쇼핑하기", 90),
                    RecapDetailResponse.TimelineResponse("20:00", "20:35", "신발 쇼핑하기", 35),
                    RecapDetailResponse.TimelineResponse("21:00", "22:30", "신발 쇼핑하기", 90)
                ),
            topics =
                listOf(
                    RecapDetailResponse.TopicResponse(
                        "손흥민",
                        "손흥민 & 주식시장",
                        "오늘은 스포츠 뉴스와 주식 정보를 자주 확인하셨네요. 시장 동향에 관심이 많으신 것 같습니다."
                    ),
                    RecapDetailResponse.TopicResponse(
                        "학습",
                        "개발 스택",
                        "React와 TypeScript 관련 자료를 집중적으로 탐색했습니다. 새로운 프로젝트를 시작하셨나요?"
                    ),
                    RecapDetailResponse.TopicResponse(
                        "주식시장",
                        "손흥민 & 주식시장",
                        "오늘은 스포츠 뉴스와 주식 정보를 자주 확인하셨네요. 시장 동향에 관심이 많으신 것 같습니다."
                    )
                )
        )
}
