package com.retoday.api.domain.user

import com.ninjasquad.springmockk.MockkBean
import com.retoday.api.common.ControllerTest
import com.retoday.api.domain.user.controller.UserController
import com.retoday.api.domain.user.dto.request.AddMyExcludedDomainRequest
import com.retoday.api.domain.user.dto.response.GetMyProfileResponse
import com.retoday.api.extension.document
import com.retoday.api.extension.expectBody
import com.retoday.api.extension.expectError
import com.retoday.api.extension.expectStatus
import com.retoday.api.extension.withAuthentication
import com.retoday.api.snippet.addMyExcludedDomainRequestFields
import com.retoday.api.snippet.errorResponseFields
import com.retoday.api.snippet.getMyProfileResponseFields
import com.retoday.core.domain.user.exception.ExcludedDomainAlreadyExistsException
import com.retoday.core.domain.user.service.UserService
import com.retoday.core.fixture.createGetMyProfileResult
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.expectBody

@WebMvcTest(UserController::class)
class UserControllerTest : ControllerTest() {
    @MockkBean
    private lateinit var userService: UserService

    init {
        describe("addMyExcludedDomain()은") {
            val requestBody = AddMyExcludedDomainRequest(domain = "github.com")
            val request =
                webClient
                    .post()
                    .uri("/users/me/excluded-domains")
                    .bodyValue(requestBody)
                    .withAuthentication()

            context("유효한 요청이 주어진 경우") {
                every { userService.addMyExcludedDomain(any(), any()) } returns Unit

                it("상태 코드 200을 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(200)
                        .expectBody<Void>()
                        .document("내 예외 도메인 추가 성공(200)") {
                            requestBody(addMyExcludedDomainRequestFields)
                        }
                }
            }

            context("이미 등록된 예외 도메인인 경우") {
                every { userService.addMyExcludedDomain(any(), any()) } throws
                    ExcludedDomainAlreadyExistsException("github.com")

                it("상태 코드 409와 ErrorResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(409)
                        .expectError()
                        .document("내 예외 도메인 추가 실패(409)") {
                            requestBody(addMyExcludedDomainRequestFields)
                            responseBody(errorResponseFields)
                        }
                }
            }

            context("도메인 형식이 올바르지 않은 경우") {
                val invalidRequest =
                    webClient
                        .post()
                        .uri("/users/me/excluded-domains")
                        .bodyValue(AddMyExcludedDomainRequest(domain = "invalid-domain"))
                        .withAuthentication()

                it("상태 코드 400과 ErrorResponse를 반환한다.") {
                    invalidRequest
                        .exchange()
                        .expectStatus(400)
                        .expectError()
                        .document("내 예외 도메인 추가 실패 - 유효하지 않은 도메인 형식(400)") {
                            requestBody(addMyExcludedDomainRequestFields)
                            responseBody(errorResponseFields)
                        }
                }
            }
        }

        describe("getMyProfile()은") {
            val request =
                webClient
                    .get()
                    .uri("/users/me/profiles")
                    .withAuthentication()

            context("유효한 요청이 주어진 경우") {
                val result = createGetMyProfileResult()

                every { userService.getMyProfile(any()) } returns result

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
