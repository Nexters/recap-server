package com.retoday.core.domain.auth.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class RefreshTokenNotFoundException : ServerException(errorType = ErrorType.REFRESH_TOKEN_NOT_FOUND)
