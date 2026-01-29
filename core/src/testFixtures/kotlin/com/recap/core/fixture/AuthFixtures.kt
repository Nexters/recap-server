package com.recap.core.fixture

import com.recap.core.domain.auth.dto.command.LoginCommand
import com.recap.core.domain.auth.dto.command.RefreshCommand
import com.recap.core.domain.auth.dto.response.GetOAuthUserResponse
import com.recap.core.domain.auth.dto.result.LoginResult
import com.recap.core.domain.auth.dto.result.RefreshResult
import com.recap.core.domain.auth.entity.RefreshToken
import com.recap.core.domain.user.entity.Provider
import java.time.Duration

const val TOKEN = "asddaadaddadsdasdasadsads"
val EXPIRATION = Duration.ofHours(1)!!

fun createRefreshToken(
    userId: Long = ID,
    content: String = TOKEN,
    expiration: Duration = EXPIRATION
): RefreshToken =
    RefreshToken(
        userId = userId,
        content = content,
        expiration = expiration
    )

fun createGetOAuthUserResponse(
    id: String = SOCIAL_ID,
    email: String = EMAIL
): GetOAuthUserResponse =
    GetOAuthUserResponse(
        id = id,
        email = email
    )

fun createLoginCommand(
    oAuthToken: String = TOKEN,
    provider: Provider = PROVIDER
): LoginCommand =
    LoginCommand(
        oAuthToken = oAuthToken,
        provider = provider
    )

fun createRefreshCommand(refreshToken: String = TOKEN): RefreshCommand = RefreshCommand(refreshToken = refreshToken)

fun createLoginResult(
    accessToken: String = TOKEN,
    refreshToken: String = TOKEN
): LoginResult =
    LoginResult(
        accessToken = accessToken,
        refreshToken = refreshToken
    )

fun createRefreshResult(
    accessToken: String = TOKEN,
    refreshToken: String = TOKEN
): RefreshResult =
    RefreshResult(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
