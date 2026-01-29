package com.recap.core.domain.auth.exception

import com.recap.core.global.exception.ServerException

class InvalidAuthenticationException(
    override val message: String = "유효하지 않은 인증 토큰입니다."
) : ServerException(status = 401, message)
