package com.retoday.core.domain.recap.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class RecapParsingException : ServerException(errorType = ErrorType.RECAP_PARSING_FAILED)
