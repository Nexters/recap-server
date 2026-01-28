package com.recap.api.domain.auth.dto.response

import com.recap.core.domain.auth.dto.result.LoginResult

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun from(result: LoginResult): LoginResponse =
            with(result) {
                LoginResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            }
    }
}
