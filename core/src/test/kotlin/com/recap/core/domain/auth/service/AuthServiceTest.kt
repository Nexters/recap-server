package com.recap.core.domain.auth.service

import com.recap.core.domain.auth.client.OAuthClient
import com.recap.core.domain.auth.exception.InvalidOAuthTokenException
import com.recap.core.domain.auth.repository.RefreshTokenRepository
import com.recap.core.domain.user.entity.User
import com.recap.core.domain.user.repository.UserRepository
import com.recap.core.fixture.*
import com.recap.core.global.jwt.JwtProvider
import com.recap.core.global.properties.JwtProperties
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

class AuthServiceTest : BehaviorSpec() {
    private val userRepository = mockk<UserRepository>()
    private val refreshTokenRepository = mockk<RefreshTokenRepository>()
    private val oAuthClient = mockk<OAuthClient>()
    private val jwtProvider =
        mockk<JwtProvider>()
            .apply {
                every { createToken(any(), any<User>()) } returns TOKEN
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
        Given("가입한 사용자가 유효한 OAuth2 토큰을 가지고 있고") {
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
                val changedEmail = "1117mg@github.com"

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

        Given("가입하지 않은 사용자가 유효한 OAuth2 토큰을 가지고 있는 경우") {
            val command = createLoginCommand()
            val user = createUser()
            val refreshToken = createRefreshToken()
            val getOAuthUserResponse = createGetOAuthUserResponse()

            every { userRepository.findBySocialIdAndProvider(any(), any()) } returns null
            every { userRepository.save(any()) } returns user
            every { refreshTokenRepository.save(any()) } returns refreshToken
            every { oAuthClient.provider } returns command.provider
            every { oAuthClient.getOAuthUserByToken(any()) } returns getOAuthUserResponse

            When("로그인을 시도하면") {
                val result = authService.login(command)

                Then("회원가입과 함께 로그인 처리가 된다.") {
                    result shouldBe createLoginResult()
                }
            }
        }

        Given("사용자가 유효하지 않은 OAuth2 토큰을 가진 경우") {
            val command = createLoginCommand()

            every { oAuthClient.provider } returns command.provider
            every { oAuthClient.getOAuthUserByToken(any()) } throws InvalidOAuthTokenException()

            When("로그인을 시도하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<InvalidOAuthTokenException> { authService.login(command) }
                }
            }
        }
    }
}
