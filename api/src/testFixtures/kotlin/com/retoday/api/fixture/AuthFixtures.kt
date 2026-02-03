package com.retoday.api.fixture

import com.retoday.api.domain.auth.dto.request.LoginRequest
import com.retoday.api.domain.auth.dto.request.RefreshRequest
import com.retoday.api.global.security.RetodayAuthentication
import com.retoday.core.domain.user.entity.Provider
import com.retoday.core.domain.user.entity.Role
import com.retoday.core.fixture.ID
import com.retoday.core.fixture.PROVIDER
import com.retoday.core.fixture.ROLES
import com.retoday.core.fixture.TOKEN

fun createRetodayAuthentication(
    id: Long = ID,
    roles: Set<Role> = ROLES
): RetodayAuthentication =
    RetodayAuthentication(
        id = id,
        roles = roles
    )

fun createLoginRequest(
    oAuthToken: String = TOKEN,
    provider: Provider = PROVIDER
): LoginRequest =
    LoginRequest(
        oAuthToken = oAuthToken,
        provider = provider
    )

fun createRefreshRequest(refreshToken: String = TOKEN): RefreshRequest = RefreshRequest(refreshToken = refreshToken)
