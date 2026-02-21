package com.retoday.api.domain.recap.controller

import com.retoday.core.domain.recap.service.RecapService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDate

class RecapControllerTest :
    BehaviorSpec({

        val recapService = mockk<RecapService>()
        val controller = RecapController(recapService)
        val mockMvc = MockMvcBuilders.standaloneSetup(controller).build()

        Given("리캡 생성 요청 시") {
            val userId = 1L
            val nickname = "민주"

            // 서비스 함수 호출에 대한 Mock 설정
            every {
                recapService.createDailyRecap(any(), any(), any())
            } just Runs

            When("날짜(date) 파라미터 없이 호출하면") {
                val response =
                    mockMvc.perform(
                        post("/api/v1/recaps/generate")
                            .param("userId", userId.toString())
                            .param("nickname", nickname)
                    )

                Then("200 OK를 반환하고 어제 날짜로 서비스가 호출된다") {
                    response.andExpect(status().isOk)

                    val yesterday = LocalDate.now().minusDays(1)
                    verify(exactly = 1) {
                        recapService.createDailyRecap(userId, nickname, yesterday)
                    }
                }
            }

            When("특정 날짜(date)를 포함하여 호출하면") {
                val targetDate = LocalDate.of(2024, 5, 20)

                val response =
                    mockMvc.perform(
                        post("/api/v1/recaps/generate")
                            .param("userId", userId.toString())
                            .param("nickname", nickname)
                            .param("date", targetDate.toString())
                    )

                Then("200 OK를 반환하고 해당 날짜로 서비스가 호출된다") {
                    response.andExpect(status().isOk)

                    verify(exactly = 1) {
                        recapService.createDailyRecap(userId, nickname, targetDate)
                    }
                }
            }
        }
    })
