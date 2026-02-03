package com.retoday.api.global.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class InvalidRequestException(
    override val message: String = ErrorType.INVALID_REQUEST.message
) : ServerException(errorType = ErrorType.INVALID_REQUEST)
