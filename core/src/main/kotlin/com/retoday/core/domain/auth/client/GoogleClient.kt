package com.retoday.core.domain.auth.client

import com.retoday.core.domain.auth.dto.response.GetOAuthUserResponse
import com.retoday.core.domain.auth.exception.InvalidOAuthTokenException
import com.retoday.core.domain.user.entity.Provider
import com.retoday.core.global.annotation.Client
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestClient
import org.springframework.web.client.requiredBody

@Client
class GoogleClient(
    private val restClient: RestClient
) : OAuthClient(provider = Provider.GOOGLE) {
    private companion object {
        const val USERINFO_ENDPOINT = "https://openidconnect.googleapis.com/v1/userinfo"
        const val ID_FIELD = "sub"
        const val EMAIL_FIELD = "email"
    }

    override fun getOAuthUserByToken(token: String): GetOAuthUserResponse =
        restClient
            .get()
            .uri(USERINFO_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_PREFIX + token)
            .retrieve()
            .onStatus({ it == HttpStatus.UNAUTHORIZED }) { _, _ -> throw InvalidOAuthTokenException() }
            .requiredBody<Map<String, *>>()
            .run {
                GetOAuthUserResponse(
                    id = get(ID_FIELD) as String,
                    email = get(EMAIL_FIELD) as String
                )
            }
}
