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
import com.retoday.core.domain.user.entity.Profile
import com.retoday.core.domain.user.entity.User
import com.retoday.core.domain.user.exception.UserNotFoundException
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.domain.user.repository.UserRepository
import com.retoday.core.global.extension.orElse
import com.retoday.core.global.jwt.JwtProvider
import com.retoday.core.global.properties.JwtProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val oAuthClients: List<OAuthClient>,
    private val jwtProvider: JwtProvider,
    private val jwtProperties: JwtProperties
) {
    @Transactional
    fun login(command: LoginCommand): LoginResult {
        val getOAuthUserResponse =
            oAuthClients
                .first { it.provider == command.provider }
                .getOAuthUserByToken(command.oAuthToken)

        val user =
            userRepository
                .findBySocialIdAndProvider(getOAuthUserResponse.id, getOAuthUserResponse.provider)
                ?.apply { synchronizeOAuthUser(getOAuthUserResponse) }
                .orElse {
                    User(
                        socialId = getOAuthUserResponse.id,
                        email = getOAuthUserResponse.email,
                        provider = getOAuthUserResponse.provider
                    )
                }.let { userRepository.save(it) }

        profileRepository
            .findByUserId(user.id)
            ?.apply { synchronizeOAuthUser(getOAuthUserResponse) }
            .orElse {
                Profile(
                    userId = user.id,
                    firstName = getOAuthUserResponse.firstName,
                    lastName = getOAuthUserResponse.lastName,
                    imageUrl = getOAuthUserResponse.imageUrl
                )
            }.let { profileRepository.save(it) }

        val (accessToken, refreshToken) = user.createTokens()

        return LoginResult(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun refresh(command: RefreshCommand): RefreshResult {
        val userId = jwtProvider.extractUserId(command.refreshToken)
        val refreshToken = refreshTokenRepository.findByIdOrNull(userId) ?: throw RefreshTokenNotFoundException()

        if (refreshToken.content != command.refreshToken) {
            refreshTokenRepository.deleteById(userId)

            throw InvalidAuthenticationException()
        }

        val (newAccessToken, newRefreshToken) =
            userRepository
                .findByIdOrNull(userId)
                ?.createTokens()
                ?: throw UserNotFoundException()

        return RefreshResult(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
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
                            userId = id,
                            content = it,
                            expiration = jwtProperties.refreshTokenExpiration.seconds
                        )
                    )
                }

        return accessToken to refreshToken
    }
}
