package com.retoday.core.domain.history.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class InvalidTimeRangeException(
    message: String
) : ServerException(
        errorType = ErrorType.INVALID_TIME_RANGE,
        customMessage = message
    )
