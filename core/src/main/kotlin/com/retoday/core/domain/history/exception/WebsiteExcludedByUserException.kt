package com.retoday.core.domain.history.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class WebsiteExcludedByUserException(
    domain: String
) : ServerException(
        errorType = ErrorType.WEBSITE_EXCLUDED,
        customMessage = "사용자가 제외한 도메인입니다: $domain"
    )
