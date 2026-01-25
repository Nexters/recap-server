package com.recap.core.domain.auth.service

import com.recap.core.domain.auth.client.GoogleClient
import com.recap.core.domain.auth.exception.InvalidOAuthTokenException
import com.recap.core.domain.user.repository.UserRepository
import com.recap.core.fixture.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class AuthServiceTest : BehaviorSpec() {
    private val userRepository = mockk<UserRepository>()
    private val oAuthClient = mockk<GoogleClient>()
    private val authService =
        AuthService(
            userRepository = userRepository,
            oAuthClients = listOf(oAuthClient),
            jwtProvider = jwtProvider,
            jwtProperties = jwtProperties
        )

    init {
        Given("사용자가 유효한 OAuth2 토큰을 가진 경우") {
            val command = createLoginCommand()
            val user = createUser()
            val getOAuthUserResponse = createGetOAuthUserResponse()

            every { userRepository.findBySocialId(user.socialId) } returns user
            every { userRepository.save(user) } returns user
            every { oAuthClient.provider } returns command.provider
            every { oAuthClient.getOAuthUserByToken(command.oAuthToken) } returns getOAuthUserResponse

            When("로그인을 시도하면") {
                val result = authService.login(command)

                Then("로그인 처리가 된다.") {
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
