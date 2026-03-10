package com.retoday.core.domain.auth.client

import com.retoday.core.domain.auth.dto.response.GetOAuthUserResponse
import com.retoday.core.domain.user.entity.SocialProvider

abstract class OAuthClient(
    val provider: SocialProvider
) {
    protected companion object {
        const val AUTHORIZATION_HEADER_PREFIX = "Bearer "
    }

    abstract fun getOAuthUserByToken(token: String): GetOAuthUserResponse
}
