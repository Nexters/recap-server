package com.retoday.core.domain.user.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class ExcludedDomainAlreadyExistsException(
    domain: String
) : ServerException(
        errorType = ErrorType.EXCLUDED_DOMAIN_ALREADY_EXISTS,
        customMessage = "이미 예외 도메인으로 등록된 도메인입니다: $domain"
    )
