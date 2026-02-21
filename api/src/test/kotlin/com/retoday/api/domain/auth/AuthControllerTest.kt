package com.retoday.api.domain.auth

import com.ninjasquad.springmockk.MockkBean
import com.retoday.api.common.ControllerTest
import com.retoday.api.domain.auth.controller.AuthController
import com.retoday.api.domain.auth.dto.response.LoginResponse
import com.retoday.api.domain.auth.dto.response.RefreshResponse
import com.retoday.api.extension.*
import com.retoday.api.fixture.createLoginRequest
import com.retoday.api.fixture.createRefreshRequest
import com.retoday.api.snippet.*
import com.retoday.core.domain.auth.exception.InvalidAuthenticationException
import com.retoday.core.domain.auth.exception.InvalidOAuthTokenException
import com.retoday.core.domain.auth.exception.RefreshTokenNotFoundException
import com.retoday.core.domain.auth.service.AuthService
import com.retoday.core.domain.user.exception.UserNotFoundException
import com.retoday.core.fixture.createLoginResult
import com.retoday.core.fixture.createRefreshResult
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.expectBody

@WebMvcTest(AuthController::class)
class AuthControllerTest : ControllerTest() {
    @MockkBean
    private lateinit var authService: AuthService

    init {
        describe("login()은") {
            val request =
                webClient
                    .post()
                    .uri("/auth/login")
                    .bodyValue(createLoginRequest())

            context("유효한 요청이 주어진 경우") {
                val result = createLoginResult()

                every { authService.login(any()) } returns result

                it("상태 코드 200과 LoginResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(200)
                        .expectBody(LoginResponse.from(result))
                        .document("로그인 성공(200)") {
                            requestBody(loginRequestFields)
                            responseBody(loginResponseFields)
                        }
                }
            }

            context("유효하지 않은 OAuth2 토큰이 주어진 경우") {
                every { authService.login(any()) } throws InvalidOAuthTokenException()

                it("상태 코드 401과 ErrorResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(401)
                        .expectError()
                        .document("로그인 실패(401)") {
                            requestBody(loginRequestFields)
                            responseBody(errorResponseFields)
                        }
                }
            }
        }

        describe("refresh()는") {
            val request =
                webClient
                    .post()
                    .uri("/auth/refresh")
                    .bodyValue(createRefreshRequest())

            context("유효한 요청이 주어진 경우") {
                val result = createRefreshResult()

                every { authService.refresh(any()) } returns result

                it("상태 코드 200과 RefreshResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(200)
                        .expectBody(RefreshResponse.from(result))
                        .document("토큰 리프레시 성공(200)") {
                            requestBody(refreshRequestFields)
                            responseBody(refreshResponseFields)
                        }
                }
            }

            context("유효하지 않은 리프레시 토큰이 주어진 경우") {
                every { authService.refresh(any()) } throws InvalidAuthenticationException()

                it("상태 코드 401과 ErrorResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(401)
                        .expectError()
                        .document("토큰 리프레시 실패(401)") {
                            requestBody(refreshRequestFields)
                            responseBody(errorResponseFields)
                        }
                }
            }

            context("로그아웃한 사용자의 리프레시 토큰이 주어진 경우") {
                every { authService.refresh(any()) } throws RefreshTokenNotFoundException()

                it("상태 코드 404와 ErrorResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(404)
                        .expectError()
                        .document("토큰 리프레시 실패(404 - 1)") {
                            requestBody(refreshRequestFields)
                            responseBody(errorResponseFields)
                        }
                }
            }

            context("탈퇴한 사용자의 유효한 리프레시 토큰이 주어진 경우") {
                every { authService.refresh(any()) } throws UserNotFoundException()

                it("상태 코드 404와 ErrorResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(404)
                        .expectError()
                        .document("토큰 리프레시 실패(404 - 2)") {
                            requestBody(refreshRequestFields)
                            responseBody(errorResponseFields)
                        }
                }
            }
        }

        describe("logout()은") {
            val request =
                webClient
                    .post()
                    .uri("/auth/logout")
                    .withAuthentication()

            context("유효한 요청이 주어진 경우") {
                every { authService.logout(any()) } just runs

                it("상태 코드 200을 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(200)
                        .expectBody<Void>()
                        .document("로그아웃 성공(200)")
                }
            }

            context("로그아웃한 사용자의 요청이 주어진 경우") {
                every { authService.logout(any()) } throws RefreshTokenNotFoundException()

                it("상태 코드 404와 ErrorResponse를 반환한다.") {
                    request
                        .exchange()
                        .expectStatus(404)
                        .expectError()
                        .document("로그아웃 실패(404)") {
                            responseBody(errorResponseFields)
                        }
                }
            }
        }
    }
}
