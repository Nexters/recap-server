package com.recap.api.fixture

import com.recap.api.domain.auth.dto.request.LoginRequest
import com.recap.core.domain.user.entity.Provider
import com.recap.core.fixture.PROVIDER
import com.recap.core.fixture.TOKEN

fun createLoginRequest(
    oAuthToken: String = TOKEN,
    provider: Provider = PROVIDER
): LoginRequest =
    LoginRequest(
        oAuthToken = oAuthToken,
        provider = provider
    )
