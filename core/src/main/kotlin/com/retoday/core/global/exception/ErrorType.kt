package com.retoday.core.global.exception

enum class ErrorType(
    val message: String,
    val status: Int
) {
    // Global
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다.", 500),
    INVALID_REQUEST("잘못된 요청입니다.", 400),
    UNAUTHENTICATED("인증되지 않은 사용자입니다.", 401),
    UNAUTHORIZED("인가되지 않은 사용자입니다.", 403),

    // Auth
    INVALID_AUTHENTICATION_TOKEN("유효하지 않은 인증 토큰입니다.", 401),
    INVALID_OAUTH_TOKEN("유효하지 않은 OAuth2 토큰입니다.", 401),
    REFRESH_TOKEN_NOT_FOUND("존재하지 않는 리프레시 토큰입니다.", 404),

    // User
    USER_NOT_FOUND("존재하지 않는 사용자입니다.", 404);

    val code: String = name
}
