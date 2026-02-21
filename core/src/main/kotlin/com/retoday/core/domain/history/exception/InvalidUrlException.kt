package com.retoday.core.domain.history.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class InvalidUrlException(
    url: String
) : ServerException(
        errorType = ErrorType.INVALID_URL,
        customMessage = "유효하지 않은 URL입니다: $url"
    )
