package com.recap.core.fixture

import com.recap.core.domain.auth.dto.command.LoginCommand
import com.recap.core.domain.auth.dto.response.GetOAuthUserResponse
import com.recap.core.domain.auth.dto.result.LoginResult
import com.recap.core.domain.user.entity.Provider
import java.time.Duration

const val TOKEN = "asddaadaddadsdasdasadsads"
val EXPIRATION = Duration.ofHours(1)!!

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

fun createLoginResult(
    accessToken: String = TOKEN,
    refreshToken: String = TOKEN
): LoginResult =
    LoginResult(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
