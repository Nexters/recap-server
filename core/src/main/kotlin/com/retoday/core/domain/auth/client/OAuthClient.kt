package com.retoday.core.domain.auth.client

import com.retoday.core.domain.auth.dto.response.GetOAuthUserResponse
import com.retoday.core.domain.user.entity.Provider

abstract class OAuthClient(
    val provider: Provider
) {
    protected companion object {
        const val AUTHORIZATION_HEADER_PREFIX = "Bearer "
    }

    abstract fun getOAuthUserByToken(token: String): GetOAuthUserResponse
}
