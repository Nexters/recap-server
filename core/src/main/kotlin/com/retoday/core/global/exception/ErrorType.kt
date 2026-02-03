package com.retoday.core.global.exception

import org.springframework.http.HttpStatus

enum class ErrorType(
    val message: String,
    val status: HttpStatus
) {
    // Global
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED("인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("인가되지 않은 사용자입니다.", HttpStatus.FORBIDDEN),

    // Auth
    INVALID_AUTHENTICATION_TOKEN("유효하지 않은 인증 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_OAUTH_TOKEN("유효하지 않은 OAuth2 토큰입니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND("존재하지 않는 리프레시 토큰입니다.", HttpStatus.NOT_FOUND),

    // User
    USER_NOT_FOUND("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);

    val code: String = name
}
