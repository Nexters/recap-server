package com.retoday.core.domain.recap.service

import com.retoday.core.domain.recap.client.RecapAIClient
import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.GeminiRecapResponse
import com.retoday.core.fixture.createGeminiRecapResponse
import com.retoday.core.fixture.createUserActivities
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class RecapServiceTest :
    BehaviorSpec({

        val recapAIClient = mockk<RecapAIClient>()
        val recapService = RecapService(recapAIClient)

        val nickname = "민주"
        val activities = createUserActivities()

        Given("사용자가 활동 데이터를 가지고 있을 때") {

            And("오늘 리캡 생성을 요청하면") {

                val recapResponse = createGeminiRecapResponse()

                every {
                    recapAIClient.generate(
                        RecapType.TODAY_RECAP,
                        nickname,
                        activities,
                        GeminiRecapResponse::class.java
                    )
                } returns recapResponse

                When("generateRecap을 호출하면") {
                    val result = recapService.generateRecap(nickname, activities)

                    Then("AIClient를 통해 응답을 받아 반환한다") {
                        result shouldBe recapResponse
                    }
                }
            }
        }
    })
