package com.recap.api.domain.auth.dto.response

import com.recap.core.domain.auth.dto.result.RefreshResult

data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun from(result: RefreshResult): RefreshResponse =
            with(result) {
                RefreshResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            }
    }
}
