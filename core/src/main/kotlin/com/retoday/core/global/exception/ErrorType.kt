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
    USER_NOT_FOUND("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),

    // History
    DUPLICATE_HISTORY("이미 저장된 히스토리입니다.", HttpStatus.CONFLICT),
    INVALID_URL("유효하지 않은 URL입니다", HttpStatus.BAD_REQUEST),
    RATE_LIMIT_EXCEEDED("요청 제한을 초과했습니다.", HttpStatus.TOO_MANY_REQUESTS),
    INVALID_TIME_RANGE("유효하지 않은 시간 범위입니다", HttpStatus.BAD_REQUEST),
    WEBSITE_EXCLUDED("사용자가 제외한 도메인입니다", HttpStatus.NO_CONTENT),
    WEBSITE_CATEGORY_CLASSIFICATION_FAILED("카테고리 분류에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // Recap
    RECAP_GENERATION_FAILED("리캡 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    RECAP_RESPONSE_EMPTY("AI 응답이 비어있습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    RECAP_PARSING_FAILED("AI 응답 파싱에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    PROMPT_FILE_NOT_FOUND("리캡 프롬프트 파일을 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    val code: String = name
}
