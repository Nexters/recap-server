package com.retoday.core.domain.user.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class UserNotFoundException : ServerException(errorType = ErrorType.USER_NOT_FOUND)
