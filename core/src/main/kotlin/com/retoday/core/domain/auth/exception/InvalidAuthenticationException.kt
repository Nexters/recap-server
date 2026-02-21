package com.retoday.core.domain.auth.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class InvalidAuthenticationException : ServerException(errorType = ErrorType.INVALID_AUTHENTICATION_TOKEN)
