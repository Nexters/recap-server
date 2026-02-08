package com.retoday.core.domain.auth.service

import com.retoday.core.domain.auth.client.OAuthClient
import com.retoday.core.domain.auth.exception.InvalidAuthenticationException
import com.retoday.core.domain.auth.exception.InvalidOAuthTokenException
import com.retoday.core.domain.auth.exception.RefreshTokenNotFoundException
import com.retoday.core.domain.auth.repository.RefreshTokenRepository
import com.retoday.core.domain.user.entity.User
import com.retoday.core.domain.user.exception.UserNotFoundException
import com.retoday.core.domain.user.repository.UserRepository
import com.retoday.core.fixture.*
import com.retoday.core.global.jwt.JwtProvider
import com.retoday.core.global.properties.JwtProperties
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull

class AuthServiceTest : BehaviorSpec() {
    private val userRepository = mockk<UserRepository>()
    private val refreshTokenRepository = mockk<RefreshTokenRepository>()
    private val oAuthClient = mockk<OAuthClient>()
    private val jwtProvider =
        mockk<JwtProvider>()
            .apply {
                every { createToken(any(), any<User>()) } returns TOKEN
                every { extractUserId(any()) } returns ID
            }
    private val jwtProperties =
        mockk<JwtProperties>()
            .apply {
                every { accessTokenExpiration } returns EXPIRATION
                every { refreshTokenExpiration } returns EXPIRATION
            }
    private val authService =
        AuthService(
            userRepository = userRepository,
            refreshTokenRepository = refreshTokenRepository,
            oAuthClients = listOf(oAuthClient),
            jwtProvider = jwtProvider,
            jwtProperties = jwtProperties
        )

    init {
        Given("가입한 사용자가") {
            val command = createLoginCommand()
            val user = createUser()
            val refreshToken = createRefreshToken()
            val userSlot = slot<User>()

            every { userRepository.findBySocialIdAndProvider(any(), any()) } returns user
            every { userRepository.save(capture(userSlot)) } returns user
            every { refreshTokenRepository.save(any()) } returns refreshToken
            every { oAuthClient.provider } returns command.provider

            And("소셜 이메일이 사용자 이메일과 같은 경우") {
                every { oAuthClient.getOAuthUserByToken(any()) } returns createGetOAuthUserResponse()

                When("로그인을 시도하면") {
                    val result = authService.login(command)

                    Then("로그인 처리가 된다.") {
                        result shouldBe createLoginResult()
                    }
                }
            }

            And("소셜 이메일이 사용자 이메일과 다른 경우") {
                val changedEmail = "1117mg@re-today.com"

                every { oAuthClient.getOAuthUserByToken(any()) } returns
                    createGetOAuthUserResponse(email = changedEmail)

                When("로그인을 시도하면") {
                    val result = authService.login(command)

                    Then("사용자 이메일이 변경되고 로그인 처리가 된다.") {
                        userSlot.captured.email shouldBe changedEmail
                        result shouldBe createLoginResult()
                    }
                }
            }
        }

        Given("가입하지 않은 사용자가") {
            val command = createLoginCommand()
            val user = createUser()
            val refreshToken = createRefreshToken()
            val getOAuthUserResponse = createGetOAuthUserResponse()
            val userSlot = slot<User>()

            every { userRepository.findBySocialIdAndProvider(any(), any()) } returns null
            every { userRepository.save(capture(userSlot)) } returns user
            every { refreshTokenRepository.save(any()) } returns refreshToken
            every { oAuthClient.provider } returns command.provider
            every { oAuthClient.getOAuthUserByToken(any()) } returns getOAuthUserResponse

            When("로그인을 시도하면") {
                val result = authService.login(command)

                Then("회원가입과 함께 로그인 처리가 된다.") {
                    userSlot.captured.id shouldBe null
                    result shouldBe createLoginResult()
                }
            }
        }

        Given("사용자가 유효하지 않은 OAuth2 토큰을 가지고") {
            val command = createLoginCommand()

            every { oAuthClient.provider } returns command.provider
            every { oAuthClient.getOAuthUserByToken(any()) } throws InvalidOAuthTokenException()

            When("로그인을 시도하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<InvalidOAuthTokenException> { authService.login(command) }
                }
            }
        }

        Given("로그인한 사용자가") {
            val command = createRefreshCommand()
            val user = createUser()
            val refreshToken = createRefreshToken()

            every { userRepository.findByIdOrNull(any()) } returns user
            every { refreshTokenRepository.findByIdOrNull(any()) } returns refreshToken
            every { refreshTokenRepository.save(any()) } returns refreshToken
            every { refreshTokenRepository.delete(any()) } just runs

            And("로그인 시점에 발급된 리프레시 토큰을 가지고") {
                When("토큰 리프레시를 시도하면") {
                    val result = authService.refresh(command)

                    Then("리프레시 토큰이 재발급된다.") {
                        result shouldBe createRefreshResult()
                    }
                }
            }

            And("로그인 시점에 발급된 리프레시 토큰과 다른 리프레시 토큰을 가지고") {
                val storedRefreshToken = "dsadadasdsdsdsdsdsdsadsadads"

                every { refreshTokenRepository.findByIdOrNull(any()) } returns
                    createRefreshToken(content = storedRefreshToken)
                every { refreshTokenRepository.deleteById(any()) } just runs

                When("토큰 리프레시를 시도하면") {
                    Then("저장된 리프레시 토큰이 삭제되고 예외가 발생한다.") {
                        shouldThrow<InvalidAuthenticationException> { authService.refresh(command) }
                        verify { refreshTokenRepository.deleteById(any()) }
                    }
                }
            }

            When("로그아웃을 시도하면") {
                authService.logout(user.id!!)

                Then("로그아웃 처리가 된다.") {
                    verify { refreshTokenRepository.delete(any()) }
                }
            }
        }

        Given("로그아웃한 사용자가") {
            val user = createUser()

            every { userRepository.findByIdOrNull(any()) } returns user
            every { refreshTokenRepository.findByIdOrNull(any()) } returns null

            And("유효한 리프레시 토큰을 가지고") {
                val command = createRefreshCommand()

                When("토큰 리프레시를 시도하면") {
                    Then("예외가 발생한다.") {
                        shouldThrow<RefreshTokenNotFoundException> { authService.refresh(command) }
                    }
                }
            }

            When("로그아웃을 시도하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<RefreshTokenNotFoundException> { authService.logout(user.id!!) }
                }
            }
        }

        Given("탈퇴한 사용자가") {
            val command = createRefreshCommand()

            every { userRepository.findByIdOrNull(any()) } returns null

            And("유효한 리프레시 토큰을 가지고") {
                val refreshToken = createRefreshToken()

                every { refreshTokenRepository.findByIdOrNull(any()) } returns refreshToken

                When("토큰 리프레시를 시도하면") {
                    Then("예외가 발생한다.") {
                        shouldThrow<UserNotFoundException> { authService.refresh(command) }
                    }
                }
            }
        }

        Given("사용자가 유효하지 않은 리프레시 토큰을 가지고") {
            val command = createRefreshCommand()

            every { jwtProvider.extractUserId(any()) } throws InvalidAuthenticationException()

            When("토큰 리프레시를 시도하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<InvalidAuthenticationException> { authService.refresh(command) }
                }
            }
        }
    }
}
