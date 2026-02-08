package com.retoday.api.domain.user

import com.ninjasquad.springmockk.MockkBean
import com.retoday.api.common.ControllerTest
import com.retoday.api.domain.user.controller.UserController
import com.retoday.api.domain.user.dto.response.GetMyProfileResponse
import com.retoday.api.extension.document
import com.retoday.api.extension.expectBody
import com.retoday.api.extension.expectStatus
import com.retoday.api.extension.withAuthentication
import com.retoday.api.snippet.getMyProfileResponseFields
import com.retoday.core.domain.user.service.UserService
import com.retoday.core.fixture.createGetProfileByUserIdResult
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

@WebMvcTest(UserController::class)
class UserControllerTest : ControllerTest() {
    @MockkBean
    private lateinit var userService: UserService

    init {
        describe("getMyProfile()은") {
            val request =
                webClient
                    .get()
                    .uri("/users/me/profiles")
                    .withAuthentication()

            context("유효한 요청이 주어진 경우") {
                val result = createGetProfileByUserIdResult()

                every { userService.getProfileByUserId(any()) } returns result

                it("상태 코드 200과 GetMyProfileResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(200)
                        .expectBody(GetMyProfileResponse.from(result))
                        .document("내 프로필 조회 성공(200)") {
                            responseBody(getMyProfileResponseFields)
                        }
                }
            }
        }
    }
}
