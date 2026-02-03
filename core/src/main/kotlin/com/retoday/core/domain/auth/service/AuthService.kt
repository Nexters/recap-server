package com.retoday.core.domain.auth.service

import com.retoday.core.domain.auth.client.OAuthClient
import com.retoday.core.domain.auth.dto.command.LoginCommand
import com.retoday.core.domain.auth.dto.command.RefreshCommand
import com.retoday.core.domain.auth.dto.result.LoginResult
import com.retoday.core.domain.auth.dto.result.RefreshResult
import com.retoday.core.domain.auth.entity.RefreshToken
import com.retoday.core.domain.auth.exception.InvalidAuthenticationException
import com.retoday.core.domain.auth.exception.RefreshTokenNotFoundException
import com.retoday.core.domain.auth.repository.RefreshTokenRepository
import com.retoday.core.domain.user.entity.User
import com.retoday.core.domain.user.exception.UserNotFoundException
import com.retoday.core.domain.user.repository.UserRepository
import com.retoday.core.global.jwt.JwtProvider
import com.retoday.core.global.properties.JwtProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val oAuthClients: List<OAuthClient>,
    private val jwtProvider: JwtProvider,
    private val jwtProperties: JwtProperties
) {
    @Transactional
    fun login(command: LoginCommand): LoginResult =
        with(command) {
            val getOAuthUserResponse =
                oAuthClients
                    .first { it.provider == provider }
                    .getOAuthUserByToken(oAuthToken)
            val user =
                userRepository
                    .findBySocialIdAndProvider(getOAuthUserResponse.id, provider)
                    ?.apply { email = getOAuthUserResponse.email }
                    ?: User(
                        socialId = getOAuthUserResponse.id,
                        email = getOAuthUserResponse.email,
                        provider = provider
                    )
            val (accessToken, refreshToken) =
                userRepository
                    .save(user)
                    .createTokens()

            LoginResult(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }

    fun refresh(command: RefreshCommand): RefreshResult =
        with(command) {
            val userId = jwtProvider.extractUserId(refreshToken)

            refreshTokenRepository
                .findByIdOrNull(userId)
                ?.apply {
                    if (content != refreshToken) {
                        refreshTokenRepository.deleteById(userId)

                        throw InvalidAuthenticationException()
                    }
                }
                ?: throw RefreshTokenNotFoundException()

            val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
            val (accessToken, refreshToken) = user.createTokens()

            return RefreshResult(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }

    fun logout(userId: Long) {
        refreshTokenRepository
            .findByIdOrNull(userId)
            ?.let { refreshTokenRepository.delete(it) }
            ?: throw RefreshTokenNotFoundException()
    }

    private fun User.createTokens(): Pair<String, String> {
        val accessToken = jwtProvider.createToken(jwtProperties.accessTokenExpiration, this)
        val refreshToken =
            jwtProvider
                .createToken(jwtProperties.refreshTokenExpiration, this)
                .also {
                    refreshTokenRepository.save(
                        RefreshToken(
                            userId = id!!,
                            content = it,
                            expiration = jwtProperties.refreshTokenExpiration
                        )
                    )
                }

        return accessToken to refreshToken
    }
}
