package com.retoday.core.fixture

import com.retoday.core.domain.auth.dto.command.LoginCommand
import com.retoday.core.domain.auth.dto.command.RefreshCommand
import com.retoday.core.domain.auth.dto.response.GetOAuthUserResponse
import com.retoday.core.domain.auth.dto.result.LoginResult
import com.retoday.core.domain.auth.dto.result.RefreshResult
import com.retoday.core.domain.auth.entity.RefreshToken
import com.retoday.core.domain.user.entity.Provider
import java.time.Duration

const val TOKEN = "eyJhbGciOiJub25lIn0.eyJpZCI6MSwiaWF0IjoxNTE2MjM5MDIyfQ."
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
    provider: Provider = PROVIDER,
    email: String = EMAIL,
    firstName: String = FIRST_NAME,
    lastName: String = LAST_NAME,
    imageUrl: String = IMAGE_URL
): GetOAuthUserResponse =
    GetOAuthUserResponse(
        id = id,
        provider = provider,
        email = email,
        firstName = firstName,
        lastName = lastName,
        imageUrl = imageUrl
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
