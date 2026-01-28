package com.recap.core.domain.auth.service

import com.recap.core.domain.auth.client.OAuthClient
import com.recap.core.domain.auth.dto.command.LoginCommand
import com.recap.core.domain.auth.dto.result.LoginResult
import com.recap.core.domain.auth.entity.RefreshToken
import com.recap.core.domain.auth.repository.RefreshTokenRepository
import com.recap.core.domain.user.entity.User
import com.recap.core.domain.user.repository.UserRepository
import com.recap.core.global.jwt.JwtProvider
import com.recap.core.global.properties.JwtProperties
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

            userRepository.save(user)

            val accessToken = jwtProvider.createToken(jwtProperties.accessTokenExpiration, user)
            val refreshToken =
                jwtProvider
                    .createToken(jwtProperties.refreshTokenExpiration, user)
                    .also {
                        refreshTokenRepository.save(
                            RefreshToken(
                                userId = user.id!!,
                                content = it,
                                expiration = jwtProperties.refreshTokenExpiration
                            )
                        )
                    }

            LoginResult(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
}
