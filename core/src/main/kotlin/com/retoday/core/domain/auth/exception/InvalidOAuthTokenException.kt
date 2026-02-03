package com.retoday.core.domain.auth.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class InvalidOAuthTokenException : ServerException(errorType = ErrorType.INVALID_OAUTH_TOKEN)
