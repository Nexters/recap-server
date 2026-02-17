package com.retoday.api.snippet

import com.retoday.api.domain.auth.dto.request.LoginRequest
import com.retoday.api.domain.auth.dto.request.RefreshRequest
import com.retoday.api.domain.auth.dto.response.LoginResponse
import com.retoday.api.domain.auth.dto.response.RefreshResponse
import com.retoday.api.extension.desc
import com.retoday.api.extension.fieldsOf

val loginRequestFields =
    fieldsOf(
        LoginRequest::oAuthToken desc "OAuth2 토큰",
        LoginRequest::provider desc "OAuth2 제공자"
    )

val refreshRequestFields =
    fieldsOf(
        RefreshRequest::refreshToken desc "리프레시 토큰"
    )

val loginResponseFields =
    fieldsOf(
        LoginResponse::accessToken desc "액세스 토큰",
        LoginResponse::refreshToken desc "리프레시 토큰"
    )

val refreshResponseFields =
    fieldsOf(
        RefreshResponse::accessToken desc "액세스 토큰",
        RefreshResponse::refreshToken desc "리프레시 토큰"
    )
