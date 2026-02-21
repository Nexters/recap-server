package com.retoday.core.domain.recap.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class RecapPromptNotFoundException : ServerException(ErrorType.PROMPT_FILE_NOT_FOUND)
