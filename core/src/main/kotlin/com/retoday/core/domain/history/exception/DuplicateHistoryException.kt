package com.retoday.core.domain.history.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class DuplicateHistoryException(
    tabId: Int,
    url: String
) : ServerException(
        errorType = ErrorType.DUPLICATE_HISTORY,
        customMessage = "이미 저장된 히스토리입니다. tabId: $tabId, url: $url"
    )
