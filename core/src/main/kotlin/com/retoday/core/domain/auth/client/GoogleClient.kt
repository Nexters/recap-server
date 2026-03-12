package com.retoday.core.domain.auth.client

import com.retoday.core.domain.auth.dto.response.GetOAuthUserResponse
import com.retoday.core.domain.auth.exception.InvalidOAuthTokenException
import com.retoday.core.domain.user.entity.SocialProvider
import com.retoday.core.global.annotation.Client
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestClient
import org.springframework.web.client.requiredBody

@Client
class GoogleClient(
    private val restClient: RestClient
) : OAuthClient(provider = SocialProvider.GOOGLE) {
    private companion object {
        const val USERINFO_ENDPOINT = "https://openidconnect.googleapis.com/v1/userinfo"
        const val ID_FIELD = "sub"
        const val EMAIL_FIELD = "email"
        const val FIRST_NAME_FIELD = "given_name"
        const val LAST_NAME_FIELD = "family_name"
        const val IMAGE_URL_FIELD = "picture"
    }

    override fun getOAuthUserByToken(token: String): GetOAuthUserResponse =
        restClient
            .get()
            .uri(USERINFO_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_PREFIX + token)
            .retrieve()
            .onStatus({ it == HttpStatus.UNAUTHORIZED }) { _, _ -> throw InvalidOAuthTokenException() }
            .requiredBody<Map<String, String>>()
            .run {
                GetOAuthUserResponse(
                    id = getValue(ID_FIELD),
                    provider = provider,
                    email = getValue(EMAIL_FIELD),
                    firstName = getValue(FIRST_NAME_FIELD),
                    lastName = getValue(LAST_NAME_FIELD),
                    imageUrl = getValue(IMAGE_URL_FIELD)
                )
            }
}
