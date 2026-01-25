package com.recap.core.fixture

import com.recap.core.domain.auth.dto.command.LoginCommand
import com.recap.core.domain.auth.dto.response.GetOAuthUserResponse
import com.recap.core.domain.auth.dto.result.LoginResult
import com.recap.core.domain.user.entity.Provider
import com.recap.core.domain.user.entity.Role
import com.recap.core.domain.user.entity.User

const val SOCIAL_ID = "1232342423"
const val EMAIL = "earlgrey02@github.com"
val PROVIDER = Provider.GOOGLE
val ROLES = setOf(Role.MEMBER)
const val IS_ACTIVE = true

fun createUser(
    id: Long? = ID,
    socialId: String = SOCIAL_ID,
    email: String = EMAIL,
    provider: Provider = PROVIDER,
    roles: Set<Role> = ROLES,
    isActive: Boolean = IS_ACTIVE
): User =
    User(
        id = id,
        socialId = socialId,
        email = email,
        provider = provider,
        roles = roles,
        isActive = isActive
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

fun createLoginResult(
    accessToken: String = TOKEN,
    refreshToken: String = TOKEN
): LoginResult =
    LoginResult(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
