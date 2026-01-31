package com.retoday.api.fixture

import com.retoday.api.domain.auth.dto.request.LoginRequest
import com.retoday.api.domain.auth.dto.request.RefreshRequest
import com.retoday.core.domain.user.entity.Provider
import com.retoday.core.fixture.PROVIDER
import com.retoday.core.fixture.TOKEN

fun createLoginRequest(
    oAuthToken: String = TOKEN,
    provider: Provider = PROVIDER
): LoginRequest =
    LoginRequest(
        oAuthToken = oAuthToken,
        provider = provider
    )

fun createRefreshRequest(refreshToken: String = TOKEN): RefreshRequest = RefreshRequest(refreshToken = refreshToken)
