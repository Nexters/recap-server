package com.recap.api.domain.auth

import com.ninjasquad.springmockk.MockkBean
import com.recap.api.common.ControllerTest
import com.recap.api.domain.auth.controller.AuthController
import com.recap.api.domain.auth.dto.response.LoginResponse
import com.recap.api.fixture.createLoginRequest
import com.recap.api.snippet.errorResponseFields
import com.recap.api.snippet.loginRequestFields
import com.recap.api.snippet.loginResponseFields
import com.recap.api.util.document
import com.recap.api.util.expectBody
import com.recap.api.util.expectError
import com.recap.core.domain.auth.exception.InvalidOAuthTokenException
import com.recap.core.domain.auth.service.AuthService
import com.recap.core.fixture.createLoginResult
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

@WebMvcTest(AuthController::class)
class AuthControllerTest : ControllerTest() {
    @MockkBean
    private lateinit var authService: AuthService

    init {
        describe("login()은") {
            val uri = "/api/v1/auth/login"
            val request = createLoginRequest()
            val result = createLoginResult()

            context("유효한 OAuth2 토큰이 주어진 경우") {
                every { authService.login(request.toCommand()) } returns result

                it("상태 코드 200과 LoginResponse를 반환한다.") {
                    webClient
                        .post()
                        .uri(uri)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus()
                        .isOk
                        .expectBody<LoginResponse>(LoginResponse.from(result))
                        .document("로그인 성공(200)") {
                            requestBody(loginRequestFields)
                            responseBody(loginResponseFields)
                        }
                }
            }

            context("유효하지 않은 OAuth2 토큰이 주어진 경우") {
                every { authService.login(request.toCommand()) } throws InvalidOAuthTokenException()

                it("상태 코드 401과 ErrorResponse를 반환한다.") {
                    webClient
                        .post()
                        .uri(uri)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus()
                        .isUnauthorized
                        .expectError()
                        .document("로그인 실패(401)") {
                            requestBody(loginRequestFields)
                            responseBody(errorResponseFields)
                        }
                }
            }
        }
    }
}
