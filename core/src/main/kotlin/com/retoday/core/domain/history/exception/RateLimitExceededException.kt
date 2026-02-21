package com.retoday.core.domain.history.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class RateLimitExceededException(
    userId: Long,
    val retryAfterSeconds: Long
) : ServerException(
        errorType = ErrorType.RATE_LIMIT_EXCEEDED,
        customMessage = "요청 제한을 초과했습니다. userId: $userId, retryAfter: ${retryAfterSeconds}초"
    )
